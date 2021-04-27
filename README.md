Dado un AFD se requiere que implemente el proceso de minimización y,
posteriormente, evaluar una cadena de entrada y decidir si es aceptada o rechazada por
dicho autómata.

## Entrada

Se recibirá una línea con un entero N > 0 que indica el número de casos de prueba.
Luego se recibirá una línea de enteros separados por espacios que indica los estados del
AFD a minimizar, seguido de una línea de enteros separados por espacios que indica sus
estados finales. Posteriormente se recibirán tantas líneas como transiciones posea el AFD
de la forma O D C, donde O representa el estado de origen de la transición, D representa el
estado destino y C representa el carácter asociado a la transición. Finalmente, se recibirán
tantas líneas como entradas quieran evaluarse en el autómata. Cada caso de prueba termina
cuando se recibe una línea con la palabra FC. La cadena vacía será representada con la
palabra NUL.

## Salida

Cada caso comenzará con la línea “Caso C:”, donde C representa el número de caso
correspondiente. Seguidamente, se escribirá una línea con los estados del autómata
minimizado en orden ascendente separados por espacios. Seguido de esto, se escribirá una
línea con los estados finales del autómata mínimo separados por espacios y en orden
ascendente. Posteriormente, se escribirán las transiciones del autómata minimizado en
orden ascendente. Finalmente, se escribirán tantas líneas como cadenas a evaluar se hayan
recibido, cada una indicando “Aceptada” o “Rechazada”, según sea el caso. Cada caso debe
terminar con una línea en blanco. Los estados del autómata minimizado que resulten de la
unión de 2 o más estados del autómata original se separan con un guión (-).
