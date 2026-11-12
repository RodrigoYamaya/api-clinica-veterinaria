from sqlalchemy.orm import Session
from sqlalchemy import select
from sqlalchemy.exc import IntegrityError
from models.vacinacao_model import VacinacaoGoverno

def buscar_por_idempotency_key(db: Session, idempotency_key: str) -> VacinacaoGoverno | None:
    return db.scalar(select(VacinacaoGoverno).where(VacinacaoGoverno.idempotency_key == idempotency_key))

def buscar_por_protocolo(db: Session, protocolo: str) -> VacinacaoGoverno | None:
    return db.scalar(select(VacinacaoGoverno).where(VacinacaoGoverno.protocolo == protocolo))

def listar_todas(db: Session):
    return db.scalars(select(VacinacaoGoverno).order_by(VacinacaoGoverno.recebido_em.desc())).all()

def salvar_vacinacao(db: Session, vacinacao: VacinacaoGoverno) -> VacinacaoGoverno:
    db.add(vacinacao)
    try:
        db.commit()
        db.refresh(vacinacao)
        return vacinacao
    except IntegrityError:
        db.rollback()
        raise