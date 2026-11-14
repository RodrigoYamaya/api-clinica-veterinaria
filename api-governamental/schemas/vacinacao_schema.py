from pydantic import BaseModel, Field
from datetime import date

class VacinacaoIn(BaseModel):
    codigo_microchip: str = Field(min_length=5, max_length=30)
    nome_animal: str = Field(min_length=1, max_length=100)
    data_aplicacao: date
    nome_vacina: str = Field(min_length=1, max_length=100)
    fabricante: str = Field(min_length=1, max_length=100)
    numero_lote: str = Field(min_length=1, max_length=50)
    veterinario_responsavel: str = Field(min_length=1, max_length=150)