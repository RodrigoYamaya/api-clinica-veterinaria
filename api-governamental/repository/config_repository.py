from sqlalchemy.orm import Session
from models.config_model import ConfiguracaoServico

def obter_configuracao(db: Session) -> ConfiguracaoServico | None:
    return db.get(ConfiguracaoServico, 1)

def salvar_configuracao(db: Session, config: ConfiguracaoServico) -> ConfiguracaoServico:
    db.add(config)
    db.commit()
    db.refresh(config)
    return config