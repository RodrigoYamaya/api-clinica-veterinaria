from datetime import datetime
from sqlalchemy import Boolean, DateTime
from sqlalchemy.orm import Mapped, mapped_column
from database.connection import Base

class ConfiguracaoServico(Base):
    __tablename__ = "configuracao_servico"
    
    id: Mapped[int] = mapped_column(primary_key=True)
    indisponivel: Mapped[bool] = mapped_column(Boolean, default=False)
    atualizado_em: Mapped[datetime] = mapped_column(DateTime, default=datetime.utcnow, onupdate=datetime.utcnow)