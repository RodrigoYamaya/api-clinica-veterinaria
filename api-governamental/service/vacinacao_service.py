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

    # Regra de Idempotência (Evitar duplicidade) tmr
    existente = vacinacao_repository.buscar_por_idempotency_key(db, idempotency_key)
    if existente:
        return {
            "sucesso": True,
            "duplicada": True,
            "protocolo": existente.protocolo,
            "status": "CONFIRMADA",
            "mensagem": "Operação já processada anteriormente; resultado original reutilizado."
        }

    animal = animal_repository.buscar_por_microchip(db, data.codigo_microchip)
    animal_id = animal.id if animal else None

    protocolo = f"GOV-{datetime.utcnow():%Y%m%d}-{uuid4().hex[:10].upper()}"

    obj = VacinacaoGoverno(
        protocolo=protocolo,
        animal_id=animal_id,
        codigo_microchip=data.codigo_microchip,
        nome_animal=data.nome_animal,
        data_aplicacao=data.data_aplicacao.isoformat(),
        nome_vacina=data.nome_vacina,
        fabricante=data.fabricante,
        numero_lote=data.numero_lote,
        veterinario_responsavel=data.veterinario_responsavel,
        idempotency_key=idempotency_key
    )

    try:
        obj = vacinacao_repository.salvar_vacinacao(db, obj)
    except IntegrityError:
        existente = vacinacao_repository.buscar_por_idempotency_key(db, idempotency_key)
        if not existente:
            raise HTTPException(status_code=409, detail="Conflito ao registrar operação; tente consultar novamente")
        return {
            "sucesso": True,
            "duplicada": True,
            "protocolo": existente.protocolo,
            "status": "CONFIRMADA",
            "mensagem": "Operação já processada anteriormente; resultado original reutilizado."
        }

    result = {
        "sucesso": True,
        "duplicada": False,
        "protocolo": obj.protocolo,
        "status": "CONFIRMADA",
        "mensagem": "Vacinação antirrábica registrada com sucesso.",
        "recebidoEm": obj.recebido_em.isoformat()
    }

    obj.resposta_json = json.dumps(result, ensure_ascii=False)
    vacinacao_repository.salvar_vacinacao(db, obj)

    return result

def consultar_operacao(db: Session, protocolo: str):
    obj = vacinacao_repository.buscar_por_protocolo(db, protocolo)
    if not obj:
        raise HTTPException(status_code=404, detail="Protocolo não encontrado")
    return {
        "protocolo": obj.protocolo,
        "status": "CONFIRMADA",
        "codigo_microchip": obj.codigo_microchip,
        "nome_animal": obj.nome_animal,
        "nome_vacina": obj.nome_vacina,
        "fabricante": obj.fabricante,
        "numero_lote": obj.numero_lote,
        "data_aplicacao": obj.data_aplicacao,
        "veterinario_responsavel": obj.veterinario_responsavel,
        "recebidoEm": obj.recebido_em
    }

def listar_vacinacoes(db: Session):
    return vacinacao_repository.listar_todas(db)