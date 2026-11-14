from datetime import datetime
from sqlalchemy import String, DateTime
from sqlalchemy.orm import Mapped, mapped_column
from database.connection import Base

class AnimalGoverno(Base):
    __tablename__ = "animais_governo"

    id: Mapped[int] = mapped_column(primary_key=True)
    microchip: Mapped[str] = mapped_column(String(30), unique=True, index=True)
    nome: Mapped[str] = mapped_column(String(100))
    especie: Mapped[str] = mapped_column(String(80))
    raca: Mapped[str | None] = mapped_column(String(100))
    data_nascimento: Mapped[str | None] = mapped_column(String(10))
    sexo: Mapped[str | None] = mapped_column(String(20))
    cor: Mapped[str | None] = mapped_column(String(60))
    tutor_nome: Mapped[str | None] = mapped_column(String(160))
    cpf_tutor: Mapped[str | None] = mapped_column(String(14))
    origem: Mapped[str] = mapped_column(String(80), default="VetCare")
    atualizado_em: Mapped[datetime] = mapped_column(DateTime, default=datetime.utcnow, onupdate=datetime.utcnow)