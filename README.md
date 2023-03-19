# Трекер задач 
## Спринт №3
Как системы контроля версий помогают команде работать с общим кодом, так и трекеры задач позволяют эффективно организовать совместную работу над задачами. Вам предстоит написать бэкенд для такого трекера. В итоге должна получиться программа, отвечающая за формирование модели данных для этой страницы:
![трекер задач](https://user-images.githubusercontent.com/63973151/226165153-9d3adf86-993f-4472-b998-04a423356844.png)

*** Типы задач ***
Простейшим кирпичиком такой системы является задача (англ. task). У задачи есть следующие свойства:
Название, кратко описывающее суть задачи (например, «Переезд»).
Описание, в котором раскрываются детали.
Уникальный идентификационный номер задачи, по которому её можно будет найти.
Статус, отображающий её прогресс. Мы будем выделять следующие этапы жизни задачи:
-NEW — задача только создана, но к её выполнению ещё не приступили.
-IN_PROGRESS — над задачей ведётся работа.
-DONE — задача выполнена.
Иногда для выполнения какой-нибудь масштабной задачи её лучше разбить на подзадачи (англ. subtask). Большую задачу, которая делится на подзадачи, мы будем называть эпиком (англ. epic). 
Таким образом, в нашей системе задачи могут быть трёх типов: обычные задачи, эпики и подзадачи. Для них должны выполняться следующие условия:
Для каждой подзадачи известно, в рамках какого эпика она выполняется.
Каждый эпик знает, какие подзадачи в него входят.
Завершение всех подзадач эпика считается завершением эпика.
