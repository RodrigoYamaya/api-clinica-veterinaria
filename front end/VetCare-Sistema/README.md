VETCARE - FRONT-END DO SISTEMA DE GESTÃO VETERINÁRIA

ARQUIVOS
- index.html: estrutura das telas
- styles.css: identidade visual e responsividade
- app.js: navegação, formulários, integração REST e ViaCEP

COMO EXECUTAR
1. Extraia a pasta do ZIP.
2. Abra a pasta no VS Code.
3. Inicie a API Java/Spring Boot e o MySQL do projeto.
4. Confirme que a API está disponível em http://localhost:8080.
5. Abra o index.html usando a extensão Live Server do VS Code.

CONFIGURAÇÃO
A URL da API fica no início do app.js:
const API_BASE = 'http://localhost:8080';
Altere caso seu backend use outra porta/endereço.

ROTAS UTILIZADAS
- /api/animais
- /api/tutores
- /veterinarios
- /api/consultas
- /api/vacinas
- /api/especies
- /api/racas
- /api/integracoes-governo
- /api/enderecos
- /api/animais/{id}/microchip
- /api/animais/{id}/microchip/historico

IMPORTANTE SOBRE OS REQUISITOS DE SEGURANÇA
Este front-end foi alinhado aos DTOs e às rotas encontradas na API enviada. O backend atual não expõe uma rota de login/JWT e a configuração de segurança está desativada. Portanto, a seleção do veterinário no formulário de microchip não é autenticação real. Para cumprir o requisito de vínculo por veterinário autenticado e ativo, implemente Spring Security/JWT e autorização no backend.

O cadastro de veterinário do backend atual recebe o campo "senha". A senha precisa ser armazenada com BCrypt no servidor; nunca faça hash apenas no JavaScript. Os campos de confirmação de e-mail/senha, data de confirmação e último acesso também precisam de suporte no backend se forem exigidos na entrega.

A integração com o governo no backend atual registra solicitações, mas a chamada real ao serviço externo ainda precisa ser implementada. A interface apresenta os registros e permite criar a solicitação conforme o DTO existente.

O ViaCEP é consultado pelo navegador através de https://viacep.com.br/ws/{CEP}/json/. Se o CEP não existir, a rede falhar ou a consulta exceder o tempo limite, os campos de endereço podem ser preenchidos manualmente.

OBSERVAÇÃO
O site depende do backend e do banco ativos para persistir dados. Se abrir sem a API, a interface será exibida, mas as operações de cadastro/listagem apresentarão erro de conexão.
