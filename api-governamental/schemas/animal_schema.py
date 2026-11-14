from pydantic import BaseModel, Field, ConfigDict
from datetime import date

class AnimalIn(BaseModel):
    microchip: str = Field(min_length=5, max_length=30)
    nome: str = Field(min_length=1, max_length=100)
    especie: str = Field(min_length=1, max_length=80)
    raca: str | None = None
    dataNascimento: date | None = None
    sexo: str | None = None
    cor: str | None = None
    tutorNome: str | None = None
    cpfTutor: str | None = None
    origem: str | None = "VetCare"

class AnimalOut(BaseModel):
    model_config = ConfigDict(from_attributes=True)
    id: int
    microchip: str
    nome: str
    especie: str
    raca: str | None
    data_nascimento: str | None
    sexo: str | None
    cor: str | None
    tutor_nome: str | None
    cpf_tutor: str | None
    origem: str
    atualizado_em: object