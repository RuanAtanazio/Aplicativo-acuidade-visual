<!-- Use this file to provide workspace-specific custom instructions to Copilot. For more details, visit https://code.visualstudio.com/docs/copilot/copilot-customization#_use-a-githubcopilotinstructionsmd-file -->

# Optimetrics Chart - Aplicativo de Testes de Visão Profissionais

Este é um aplicativo Android desenvolvido em Kotlin usando Jetpack Compose para oftalmologistas realizarem testes de visão profissionais.

## Características Principais

- **Testes de Visão Completos**: 9 tipos diferentes de testes incluindo acuidade visual, visão de cores, astigmatismo, sensibilidade ao contraste, percepção de profundidade, campo visual, visão binocular, Landolt C e Snellen
- **Interface Moderna**: Desenvolvido com Material Design 3 e Jetpack Compose
- **Suporte Multi-idioma**: Português (BR), Inglês e Espanhol
- **Temas**: Suporte a modo claro e escuro
- **Modo Tela Cheia**: Todos os testes podem ser executados em tela cheia para máxima precisão
- **Biblioteca Médica**: Informações abrangentes sobre oftalmologia por faixa etária
- **Vídeos Educativos**: Seção dedicada a fisioterapia ocular, cirurgias e conteúdo educativo

## Estrutura do Projeto

- **MainActivity**: Tela principal com navegação por abas
- **ui.tests**: Pacote contendo todos os testes de visão
- **ui.education**: Seção de vídeos educativos
- **ui.library**: Biblioteca médica completa
- **ui.settings**: Configurações do aplicativo
- **ui.about**: Informações sobre o aplicativo e desenvolvedor

## Arquitetura

- **MVVM Pattern**: Seguindo as melhores práticas do Android
- **Jetpack Compose**: Interface declarativa moderna
- **Material Design 3**: Design system mais recente do Google
- **Multi-module**: Organização clara por funcionalidades

## Desenvolvedor

**Ruan Atanazio da Silva**
- GitHub: github.com/ruanatanazio (substitua pela URL real)
- Versão: 2.1.0 PRO
- Última atualização: Julho de 2025

## Instruções de Desenvolvimento

Quando trabalhar neste projeto:

1. Mantenha a consistência visual usando Material Design 3
2. Todos os novos testes devem seguir o padrão de tela cheia
3. Strings devem ser localizadas para os 3 idiomas suportados
4. Teste sempre em modo claro e escuro
5. Siga as convenções de nomenclatura Kotlin
6. Use Jetpack Compose para toda nova UI
7. Mantenha alta precisão nos cálculos dos testes visuais
