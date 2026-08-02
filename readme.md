# 📝 Markdown Visual Toolbar for IntelliJ IDEA

Um plugin leve e intuitivo para o **IntelliJ IDEA** que adiciona uma barra de ferramentas visual de formatação e sanitização de dados no topo de qualquer arquivo `.md`.

Ideal para desenvolvedores e redatores que desejam formatar documentações em Markdown de forma rápida, sem precisar memorizar atalhos ou digitar a sintaxe manualmente toda vez.

---

## ✨ Recursos do Plugin

* 🎨 **Barra Visual Integrada:** Surge automaticamente no topo do editor de arquivos `.md`.
* 👁 **Ícones Vetoriais Adaptáveis:** Ícones limpos no padrão de editores de mercado (Notion, Typora, GitHub), compatíveis com temas claros e escuros (Darcula).
* 🧹 **Sanitizador Automático de Dados:**
    * Remove caracteres invisíveis Unicode (ex: `ZWSP` - Zero Width Space, `NBSP`).
    * Elimina tags HTML residuais vindas da web (`<div>`, `<span>`, etc.).
    * Corrige imagens presas dentro de links (`[![(alt)](img.png)](href)` ➔ `![alt](img.png)`).
    * Separa números grudados em textos de badges/metadados.
    * Normaliza espaçamentos e linhas em branco redundantes.
* ⚡ **Ações Rápidas de Formatação:**
    * **Textos:** Negrito (**B**), Itálico (*I*), Tachado (~~S~~), Título/Ciclo de Cabeçalhos (**TT**).
    * **Listas:** Marcadores (`-`), Numerada (`1.`), Lista de Tarefas (`- [ ]`).
    * **Estruturas:** Linha Divisora (`---`), Citação (`>`), Bloco de Código (``` / ` `), Tabela (`⊞`).
    * **Mídias:** Links (`🔗`) e Imagens (`🖼`).
    * **Controle:** Desfazer (Undo) e Refazer (Redo) integrados.

---

## 🛠️ Requisitos e Compatibilidade

* **IDE:** IntelliJ IDEA 2023.3 ou superior (Community / Ultimate / Android Studio / CLion / WebStorm).
* **JDK:** Java 17.
* **Gradle:** 8.1.1 (via Gradle Wrapper).

---

## 🚀 Como Executar e Compilar Localmente

### 1. Clonar o Repositório
```bash
git clone https://github.com/Patrickaalves/markdown-editor-toolbar.git
cd markdown-editor-toolbar
```

### 2. Rodar o Plugin no Ambiente de Testes
Para abrir uma instância secundária do IntelliJ com o plugin carregado em tempo real:
```bash
./gradlew runIde
```
*(No Windows, utilize `gradlew.bat runIde`)*

### 3. Gerar o Pacote de Instalação (`.zip`)
Para compilar e gerar o instalador definitivo do plugin:
```bash
./gradlew buildPlugin
```
O arquivo `.zip` final será gerado dentro do diretório:
```text
build/distributions/markdown-editor-toolbar-1.0.0.zip
```

---

## 📦 Como Instalar no Seu IntelliJ IDEA

1. Abra o seu IntelliJ IDEA principal.
2. Acesse as Configurações:
    * **Windows/Linux:** `Ctrl + Alt + S`
    * **Mac:** `Cmd + ,`
3. Vá em **Plugins**.
4. Clique no ícone da **Engrenagem ⚙️** na parte superior e selecione **Install Plugin from Disk...**
5. Selecione o arquivo `markdown-editor-toolbar-1.0.0.zip` gerado na pasta `build/distributions/`.
6. Reinicie a IDE (**Restart IDE**).

---

## 📁 Estrutura do Projeto

```text
markdown-editor-toolbar/
├── build.gradle.kts           # Configuração de compilação do Gradle e SDK IntelliJ
├── settings.gradle.kts        # Nome do projeto Gradle
├── gradle-wrapper.properties  # Fixação da versão compatível do Gradle (8.1.1)
└── src/
    └── main/
        ├── kotlin/
        │   └── com/github/markdown/toolbar/
        │       ├── MarkdownToolbarProvider.kt  # Provedor da barra na interface da IDE
        │       └── MarkdownActions.kt          # Ações dos botões e lógica de sanitização
        └── resources/
            └── META-INF/
                └── plugin.xml                  # Manifesto e dependências do plugin
```

---

## 📄 Licença

Este projeto é mantido sob a licença [MIT](LICENSE). Sinta-se livre para contribuir com melhorias e novas funcionalidades!