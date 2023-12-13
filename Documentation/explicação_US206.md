[US206] As Ship Captain, I want the list of containers to be loaded in the next port,
including container identifier, type, and load.

Para este US foi feito um SELECT das informações pedidas usando o id da viagem e a 
seqnum da stop atual.
Somando +1 ao seqnum foi encontrado a proxima Stop onde a lista de contentores irá ser carregada indo
buscar depois o Cargo Manifest associado a essa Stop. O cargo manifest irá de seguida estar associado á 
respetiva lista de contentores a qual queremos tirar a informação. Esta informação será guardada num 
sys_refcursor que irá depois ser retornado completando a pesquisa.