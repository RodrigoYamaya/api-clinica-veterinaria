from fastapi import APIRouter, Depends, HTTPException, Header, Response
from sqlalchemy.orm import Session

from database.connection import get_db
from schemas.animal_schema import AnimalIn
from schemas.vacinacao_schema import VacinacaoIn
from schemas.config_schema import IndisponibilidadeIn

from service import config_service, animal_service, vacinacao_service

router = APIRouter(prefix="/api/governo")

@router.get("/animais/{microchip}", tags=["Animais"])
def consultar_animal(microchip: str, db: Session = Depends(get_db)):
    config_service.ensure_available(db)
    return animal_service.consultar_animal(db, microchip)

@router.put("/animais/{microchip}", tags=["Animais"])
def atualizar_ou_cadastrar_animal(microchip: str, data: AnimalIn, db: Session = Depends(get_db)):
    config_service.ensure_available(db)
    return animal_service.atualizar_ou_cadastrar_animal(db, microchip, data)

@router.post("/vacinacoes-antirrabicas", tags=["Vacinações"])
def receber_vacinacao(
    data: VacinacaoIn, 
    response: Response, 
    idempotency_key: str | None = Header(default=None, alias="Idempotency-Key"), 
    db: Session = Depends(get_db)
):
    config_service.ensure_available(db)
    resultado = vacinacao_service.receber_vacinacao(db, data, idempotency_key)
    if resultado.get("duplicada"):
        response.status_code = 200
    return resultado

@router.get("/operacoes/{protocolo}", tags=["Operações"])
def consultar_operacao(protocolo: str, db: Session = Depends(get_db)):
    config_service.ensure_available(db)
    return vacinacao_service.consultar_operacao(db, protocolo)

@router.post("/simulacao/indisponibilidade", tags=["Simulação"])
def configurar_indisponibilidade(data: IndisponibilidadeIn, db: Session = Depends(get_db)):
    return config_service.configurar_indisponibilidade(db, data)

@router.get("/vacinacoes", tags=["Vacinações"])
def listar_vacinacoes(db: Session = Depends(get_db)):
    config_service.ensure_available(db)
    return vacinacao_service.listar_vacinacoes(db)