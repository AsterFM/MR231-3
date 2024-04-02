# MR231-3
MR231-3 converter based on MR231 converter.

Для запуска через Docker:
1. Заходим через терминал в папку с проектом;
2. Прописываем команду docker build -t mr231-3 . (точка в конце обязательна);
3. После загрузки необходимых компонентов прописываем docker run;
4. Чтобы удалить все Docker images прописываем docker rmi $(docker images -q)
