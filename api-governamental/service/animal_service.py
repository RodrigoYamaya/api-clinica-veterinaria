from fastapi import HTTPException
from sqlalchemy.orm import Session
from repository import animal_repository
from models.animal_model import AnimalGoverno
from schemas.animal_schema import AnimalIn

def consultar_animal(db: Session, microchip: str):
    animal = animal_repository.buscar_por_microchip(db, microchip)
    if not animal:
        raise HTTPException(status_code=404, detail="Animal não encontrado no cadastro governamental")
    return animal

def atualizar_ou_cadastrar_animal(db: Session, microchip: str, data: AnimalIn):
    if data.microchip != microchip:
        raise HTTPException(status_code=422, detail="O microchip da URL deve ser igual ao do corpo")
        
    animal = animal_repository.buscar_por_microchip(db, microchip)
    
    values = {
        "microchip": data.microchip, "nome": data.nome, "especie": data.especie, "raca": data.raca,
        "data_nascimento": data.dataNascimento.isoformat() if data.dataNascimento else None,
        "sexo": data.sexo, "cor": data.cor, "tutor_nome": data.tutorNome, "cpf_tutor": data.cpfTutor, "origem": data.origem
    }
    
    if animal:
        for key, value in values.items():
            setattr(animal, key, value)
        acao = "ATUALIZADO"
    else:
        animal = AnimalGoverno(**values)
        acao = "CADASTRADO"
        
    animal = animal_repository.salvar_animal(db, animal)
    return {"sucesso": True, "acao": acao, "animal": animal}