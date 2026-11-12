from fastapi import HTTPException
from sqlalchemy.orm import Session
from repository import config_repository
from models.config_model import ConfiguracaoServico
from schemas.config_schema import IndisponibilidadeIn

def ensure_available(db: Session):
    config = config_repository.obter_configuracao(db)
    if config and config.indisponivel:
        raise HTTPException(
            status_code=503, 
            detail={"codigo": "SERVICO_INDISPONIVEL", "mensagem": "Serviço governamental temporariamente indisponível. Tente novamente mais tarde."}
        )

def configurar_indisponibilidade(db: Session, data: IndisponibilidadeIn):
    config = config_repository.obter_configuracao(db)
    if not config:
        config = ConfiguracaoServico(id=1, indisponivel=data.indisponivel)
    else:
        config.indisponivel = data.indisponivel
        
    config_repository.salvar_configuracao(db, config)
    
    return {
        "disponivel": not config.indisponivel, 
        "indisponivel": config.indisponivel,
        "mensagem": "Modo de indisponibilidade ativado." if config.indisponivel else "Serviço governamental restabelecido."
    }