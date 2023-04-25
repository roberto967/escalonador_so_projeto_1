# Projeto: Algoritmos de Escalonamento de CPU

Esse projeto é a implementação de um conjunto de algoritmos de escalonamento de CPU, o programa calcula as estatisticas baseadas nesses algoritmos.

## Algoritmos implementados

    - FCFS: First-Come, First-Served
    - SJF: Shortest Job First
    - RR: Round Robin (quantum = 2)

## Entrada de dados

A entrada de dados é feita atravez do arquivo _**in.csv**_ com os processos no seguinte formato:

| tempo de chegada | duração |
| ---------------- | ------- |

## Saída de dados

A saída é composta por linhas contendo a sigla do algoritmo e os valores das metricas, no formato:

| **nome do algoritmo** | tempo médio de retorno | tempo médio de resposta | tempo medio de espera |
| --------------------- | ---------------------- | ----------------------- | --------------------- |

## Exemplos

### Exemplo 1

```
0 20
1 10
4 6
4 8
5 10
5 5
6 100
7 10
8 2
9 2
10 2
```

saída:

```
FCFS 93,7 77,8 77,8
SJF 46,9 31,0 31,0
RR 60,4 8,6 44,5
```

### Exemplo 2

```
0 20
0 10
4 6
4 8
```

saída:

```
FCFS 30,5 19,5 19,5
SJF 21,5 10,5 10,5
RR 31,5 2,0 20,5
```

### Exemplo 3

```
2 20
2 10
4 6
4 8
```

saída:

```
FCFS 31,5 20,5 20,5
SJF 22,5 11,5 11,5
RR 31,5 2,0 20,5
```

### Exemplo 4

```
0 2
0 10
4 6
4 8
```

saída:

```
FCFS 12,5 6,0 6,0
SJF 12,5 6,0 6,0
RR 15,5 1,0 9,0
```
