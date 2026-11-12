from sqlalchemy.orm import Session
from sqlalchemy import select
from models.animal_model import AnimalGoverno

def buscar_por_microchip(db: Session, microchip: str) -> AnimalGoverno | None:
    return db.scalar(select(AnimalGoverno).where(AnimalGoverno.microchip == microchip))

def salvar_animal(db: Session, animal: AnimalGoverno) -> AnimalGoverno:
    db.add(animal)
    db.commit()
    db.refresh(animal)
    return animal