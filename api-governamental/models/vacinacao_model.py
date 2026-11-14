from datetime import datetime
from sqlalchemy import String, DateTime, Text, ForeignKey, UniqueConstraint
from sqlalchemy.orm import Mapped, mapped_column
from database.connection import Base

class VacinacaoGoverno(Base):
    __tablename__ = "vacinacoes_governo"
    __table_args__ = (UniqueConstraint("idempotency_key", name="uq_governo_idempotency_key"),)

    id: Mapped[int] = mapped_column(primary_key=True)
    protocolo: Mapped[str] = mapped_column(String(40), unique=True, index=True)

    # Opcional: vincula se o animal já existir na base do governo, mas não bloqueia o registro (Regra 2)
    animal_id: Mapped[int | None] = mapped_column(ForeignKey("animais_governo.id"), nullable=True)

    # Os 7 campos obrigatórios da comunicação antirrábica que definimos no dto clinica veterinaria;
    codigo_microchip: Mapped[str] = mapped_column(String(30), index=True)
    nome_animal: Mapped[str] = mapped_column(String(100))
    data_aplicacao: Mapped[str] = mapped_column(String(10))
    nome_vacina: Mapped[str] = mapped_column(String(120))
    fabricante: Mapped[str] = mapped_column(String(120))
    numero_lote: Mapped[str] = mapped_column(String(80))
    veterinario_responsavel: Mapped[str] = mapped_column(String(150))

    idempotency_key: Mapped[str] = mapped_column(String(150), nullable=False)
    recebido_em: Mapped[datetime] = mapped_column(DateTime, default=datetime.utcnow)
    resposta_json: Mapped[str | None] = mapped_column(Text)