from pydantic import BaseModel, Field
from datetime import date

class VacinacaoIn(BaseModel):
    microchip: str = Field(min_length=5, max_length=30)
    cpfVeterinario: str = Field(pattern=r"^\d{11}$")
    crmvNumero: str
    crmvUf: str = Field(min_length=2, max_length=2)
    nomeVacina: str
    fabricante: str | None = None
    lote: str | None = None
    dataAplicacao: date | None = None
    proximaDose: date | None = None
    dose: str | None = None
    viaAplicacao: str | None = None
    clinica: str = "VetCare"