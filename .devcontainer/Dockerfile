ARG VARIANT=17
FROM mcr.microsoft.com/vscode/devcontainers/java:${VARIANT}

# scala
RUN curl -fL https://github.com/coursier/coursier/releases/latest/download/cs-x86_64-pc-linux.gz | gzip -d > cs && chmod +x cs && ./cs setup  --yes

# verilator
RUN apt-get update && apt-get install -y verilator