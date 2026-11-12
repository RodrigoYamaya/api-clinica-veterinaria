from fastapi import FastAPI, Depends
from sqlalchemy.orm import Session
import uvicorn

from database.connection import Base, engine, SessionLocal
from repository import config_repository
from models.config_model import ConfiguracaoServico
from routes import governo_api

app = FastAPI(
    title="API Governamental Simulada — VetCare",
    version="1.0.0",
    description="Serviço acadêmico para cadastro de animais e registro de vacinações antirrábicas."
)

@app.on_event("startup")
def startup():
    # Cria as tabelas no banco de dados automaticamente se não existirem;
    Base.metadata.create_all(bind=engine)
    db = SessionLocal()
    try:
        # Garante o registro padrão de configuração de disponibilidade (ID 1)
        if not config_repository.obter_configuracao(db):
            config_repository.salvar_configuracao(db, ConfiguracaoServico(id=1, indisponivel=False))
    finally:
        db.close()

@app.get("/health", tags=["Sistema"])
def health(db: Session = Depends(get_db)):
    config = config_repository.obter_configuracao(db)
    return {
        "status": "indisponivel" if config and config.indisponivel else "online", 
        "servico": "API Governamental Simulada"
    }

app.include_router(governo_api.router)

# Roda na porta 8082 para não conflitar com a sua api-clinica (Java/spring)
if __name__ == "__main__":
    uvicorn.run("main:app", host="127.0.0.1", port=8082, reload=True)