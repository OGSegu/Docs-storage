`Master` [![Test](https://github.com/OGSegu/Docs-storage/actions/workflows/gradle.yml/badge.svg?branch=main)](https://github.com/OGSegu/Docs-storage/actions/workflows/gradle.yml)  
`Development` [![Test](https://github.com/OGSegu/Docs-storage/actions/workflows/gradle.yml/badge.svg?branch=development)](https://github.com/OGSegu/Docs-storage/actions/workflows/gradle.yml)

# Docs-storage
Service that allows to store DOC, DOCS, PDF files and retrieve it by queries

## Examples
![Файл загружен](images/img_file_loaded.png)
![Файл найден](images/img_file_founded.png)

## Build and run
1. Build docker image
```bash 
docker build -t docs-storage:latest .
```
2. Run docker image
```bash 
docker run --name docs-storage -p 8080:8080 docs-storage:latest
```
