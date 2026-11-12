from datetime import datetime
from sqlalchemy import String, DateTime, Text, ForeignKey, UniqueConstraint
from sqlalchemy.orm import Mapped, mapped_column
from database.connection import Base

class VacinacaoGoverno(Base):
    __tablename__ = "vacinacoes_governo"
    __table_args__ = (UniqueConstraint("idempotency_key", name="uq_governo_idempotency_key"),)
    
    id: Mapped[int] = mapped_column(primary_key=True)
    protocolo: Mapped[str] = mapped_column(String(40), unique=True, index=True)
    animal_id: Mapped[int] = mapped_column(ForeignKey("animais_governo.id"))
    microchip: Mapped[str] = mapped_column(String(30), index=True)
    cpf_veterinario: Mapped[str] = mapped_column(String(11))
    crmv_numero: Mapped[str] = mapped_column(String(30))
    crmv_uf: Mapped[str] = mapped_column(String(2))
    nome_vacina: Mapped[str] = mapped_column(String(120))
    fabricante: Mapped[str | None] = mapped_column(String(120))
    lote: Mapped[str | None] = mapped_column(String(80))
    data_aplicacao: Mapped[str | None] = mapped_column(String(10))
    proxima_dose: Mapped[str | None] = mapped_column(String(10))
    dose: Mapped[str | None] = mapped_column(String(60))
    via_aplicacao: Mapped[str | None] = mapped_column(String(60))
    clinica: Mapped[str] = mapped_column(String(120), default="VetCare")
    idempotency_key: Mapped[str] = mapped_column(String(150), nullable=False)
    recebido_em: Mapped[datetime] = mapped_column(DateTime, default=datetime.utcnow)
    resposta_json: Mapped[str | None] = mapped_column(Text)