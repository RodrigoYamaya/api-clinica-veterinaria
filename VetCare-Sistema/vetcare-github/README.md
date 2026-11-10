# VetCare — Sistema de Gestão de Clínica Veterinária

Projeto acadêmico de um sistema web para apoiar a gestão de uma clínica veterinária.

## Estrutura do repositório

```text
sistema-clinica-veterinaria/
├── frontend/       # Interface web (HTML, CSS e JavaScript)
├── backend/        # API privada (adicionar os arquivos do backend aqui)
├── docs/           # Documentação, requisitos e diagramas do projeto
├── .gitignore
└── README.md
```

## Frontend

O frontend está em `frontend/` e contém:

- `index.html`: estrutura da interface;
- `styles.css`: estilos e apresentação visual;
- `app.js`: lógica e interações do lado do navegador.

### Como visualizar

Abra `frontend/index.html` em um navegador. As operações que dependem da API privada exigem que o backend esteja implementado, em execução e configurado com a URL correta.

## Backend e integrações

A pasta `backend/` foi reservada para a API privada do projeto. **O ZIP de origem continha somente os arquivos do frontend**, portanto nenhum código de backend foi incluído nesta organização.

De acordo com a arquitetura prevista, o frontend deve conversar com a API privada. A API privada é responsável por consultar o ViaCEP e se comunicar com a API governamental simulada; não coloque credenciais ou segredos no JavaScript do navegador.

## Documentação

Use `docs/` para guardar os requisitos, diagramas, especificações de endpoints, instruções de execução e entregas das etapas acadêmicas.

## Tecnologias do frontend

- HTML5
- CSS3
- JavaScript

> Observação: esta organização não significa que todas as integrações e funcionalidades estejam concluídas. A disponibilidade de cada recurso depende também da implementação correspondente no backend.
