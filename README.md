# magic
Mathematical magic versus randomness.

По мотивам первой задачи из файла "Seven Puzzles - Peter Winkler.pdf".

Правила игры:

Есть команда из N человек с личными номерами от 1 до N и комната с N коробками,
в которых в случайном порядке лежат по одному номера от 1 до N. В комнату по очереди
запускают по одному игроку. Каждый игрок имеет право открыть не более чем N / 2 коробок,
чтобы найти в них свой личный номер. После этого коробки закрываются, а игрок выходит
через отдельную дверь и не общается с остальной своей командой. Команда выигрывает,
если у всех игроков получается найти свои номера.

Если каждый участник команды будет открывать коробки в случайном порядке, то при
N = 100 вероятность выигрыша будет равна (1/2)^100 = 7.8886e-31, т.е. совсем мизерная.
При этом существует математически-статистическая магия, т.е. стратегия, позволяющая
выигрывать более чем в 30% случаев!

Как же можно победить неопределённость, не передавая информацию обратно в команду?
Смотрите программу, которая эмулирует поведение команд в этой игре.

Пример вывода программы:

Final Magic vs. Random vs. Stubborn score is [2 : 0 : 0] after 5 games played with 42 players.
Final Magic vs. Random vs. Stubborn score is [278643 : 4 : 0] after 1000000 games played with 17 players.
Final Magic vs. Random vs. Stubborn score is [1 : 1 : 1] after 1 games played with 1 players.
Final Magic vs. Random vs. Stubborn score is [311671 : 0 : 0] after 1000000 games played with 100 players.


PS: Подскажите, как выиграть в Спортлото? :-D
