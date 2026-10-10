# Skadi - Sistema de Monitoramento Térmico e Prevenção de Perdas

O **Skadi** é uma plataforma B2B desenvolvida para o monitoramento contínuo e inteligente de temperatura em Centros de Distribuição (CDs) de alimentos perecíveis[cite: 1, 2]. O sistema processa dados recebidos de sensores e termômetros IoT de terceiros, aplica inteligência artificial para diagnósticos térmicos, prevê a degradação de alimentos e dispara alertas hierárquicos para mitigar perdas financeiras e operacionais[cite: 1, 2].

---

## Tecnologias Utilizadas

### Backend
* **Java**: Linguagem principal para a construção da API REST, execução da lógica de negócios, integração de serviços e processamento das regras do sistema.
* **PostgreSQL**: Banco de dados relacional para a persistência de métricas térmicas, logs de alertas, gerenciamento de permissões e relatórios auditáveis.

### Frontend
* **HTML5**: Estruturação semântica do dashboard central e das interfaces de relatórios.
* **CSS**: Estilização responsiva e padronização visual focada em eficiência de navegação.
* **JavaScript**: Manipulação dinâmica do DOM, consumo de rotas REST da API e atualização de métricas em tempo real.

### UI/UX e Prototipação
* **Figma**: Prototipação de alta fidelidade, validação de fluxos de navegação e testes de usabilidade (IHC/UX).

---

## Regras de Negócio e Funcionalidades Principais

* **Monitoramento Contínuo (SaaS Agnóstico)**: Integração via API com termômetros e sensores já instalados pelo cliente nos refrigeradores, sem dependência de hardware proprietário[cite: 1, 2].
* **Inteligência Artificial e Diagnóstico**:
  * Identificação de causa raiz para oscilações térmicas (distinguindo ciclos operacionais de degelo e aberturas de porta de falhas críticas de compressor)[cite: 1, 2].
  * Cálculo dinâmico do **Tempo de Sobrevivência** da carga e estimativa percentual da perda de vida útil do alimento[cite: 1, 2].
  * Estimativa automática do impacto financeiro (lucro perdido e custos evitados)[cite: 1, 2].
* **Alertas Hierárquicos**: Escalamento de notificações por WhatsApp, SMS e e-mail com registro do operador responsável pelo reconhecimento do chamado[cite: 1, 2].
* **Conformidade Sanitária e Auditoria**: Emissão de relatórios estruturados segundo normas ANVISA (RDC 430 e Guia 33/2020), FDA, HACCP e ISO, com suporte a marca d'água digital e validação por assinatura digital ICP-Brasil[cite: 1, 2].
* **Controle de Acesso Granular**: Gerenciamento de permissões por unidade operacional ou nível de gestão[cite: 2].

---

## Integrantes do Projeto

* Daniel Pavesi Rissato[cite: 1, 2]
* Isabella Araujo de Omena[cite: 1, 2]
* Maria Luísa Passos Vieira[cite: 1, 2]
* Vinicius Guerra Menezes de Sousa[cite: 1, 2]

## Licença

Este projeto é um software de propriedade privada. O uso, replicação ou redistribuição sem autorização prévia dos desenvolvedores e mantenedores do projeto Skadi é estritamente proibido.