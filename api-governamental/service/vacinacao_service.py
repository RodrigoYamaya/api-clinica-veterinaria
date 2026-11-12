from datetime import datetime
from uuid import uuid4
import json
from fastapi import HTTPException
from sqlalchemy.orm import Session
from sqlalchemy.exc import IntegrityError

from repository import vacinacao_repository, animal_repository
from models.vacinacao_model import VacinacaoGoverno
from schemas.vacinacao_schema import VacinacaoIn

def receber_vacinacao(db: Session, data: VacinacaoIn, idempotency_key: str):
    if not idempotency_key:
        raise HTTPException(status_code=400, detail="Cabeçalho Idempotency-Key é obrigatório")
    if len(idempotency_key) > 150:
        raise HTTPException(status_code=422, detail="Idempotency-Key excede 150 caracteres")
        
    # Regra 1: Idempotência (Evitar duplicidade) que foi definida no escopo do trabalho professor
    existente = vacinacao_repository.buscar_por_idempotency_key(db, idempotency_key)
    if existente:
        return {"sucesso": True, "duplicada": True, "protocolo": existente.protocolo, "status": "CONFIRMADA", "mensagem": "Operação já processada anteriormente; resultado original reutilizado."}
        
    # Regra 2: Animal precisa existir regra
    animal = animal_repository.buscar_por_microchip(db, data.microchip)
    if not animal:
        raise HTTPException(status_code=404, detail="Microchip não cadastrado no sistema governamental")
        
    # Regra 3: Geração do Protocolo Governamental regra do escopo
    protocolo = f"GOV-{datetime.utcnow():%Y%m%d}-{uuid4().hex[:10].upper()}"
    
    obj = VacinacaoGoverno(
        protocolo=protocolo, animal_id=animal.id, microchip=data.microchip,
        cpf_veterinario=data.cpfVeterinario, crmv_numero=data.crmvNumero, crmv_uf=data.crmvUf.upper(),
        nome_vacina=data.nomeVacina, fabricante=data.fabricante, lote=data.lote,
        data_aplicacao=data.dataAplicacao.isoformat() if data.dataAplicacao else None,
        proxima_dose=data.proximaDose.isoformat() if data.proximaDose else None, dose=data.dose,
        via_aplicacao=data.viaAplicacao, clinica=data.clinica, idempotency_key=idempotency_key
    )
    
    try:
        obj = vacinacao_repository.salvar_vacinacao(db, obj)
    except IntegrityError:
        # Tratamento seguro caso duas requisições idênticas cheguem no exato mesmo milissegundo; evitar dados duplicados
        existente = vacinacao_repository.buscar_por_idempotency_key(db, idempotency_key)
        if not existente:
            raise HTTPException(status_code=409, detail="Conflito ao registrar operação; tente consultar novamente")
        return {"sucesso": True, "duplicada": True, "protocolo": existente.protocolo, "status": "CONFIRMADA"}
        
    result = {
        "sucesso": True, "duplicada": False, "protocolo": obj.protocolo, 
        "status": "CONFIRMADA", "mensagem": "Vacinação antirrábica registrada com sucesso.", 
        "recebidoEm": obj.recebido_em.isoformat()
    }
    
    # Salva o JSON da resposta para histórico
    obj.resposta_json = json.dumps(result, ensure_ascii=False)
    vacinacao_repository.salvar_vacinacao(db, obj)
    
    return result

def consultar_operacao(db: Session, protocolo: str):
    obj = vacinacao_repository.buscar_por_protocolo(db, protocolo)
    if not obj:
        raise HTTPException(status_code=404, detail="Protocolo não encontrado")
    return {
        "protocolo": obj.protocolo, "status": "CONFIRMADA", "microchip": obj.microchip, "nomeVacina": obj.nome_vacina,
        "dataAplicacao": obj.data_aplicacao, "recebidoEm": obj.recebido_em, "clinica": obj.clinica
    }

def listar_vacinacoes(db: Session):
    return vacinacao_repository.listar_todas(db)