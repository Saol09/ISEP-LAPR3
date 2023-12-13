

	[US205] As Ship Captain, I want the list of containers to be offloaded in the next port,
		    including container identifier, type, position, and load
			
			Para este US foi feito um SELECT de toda a informação pedida mais o ID de cargoManifest
			e o ID da Viagem, pois facilita na visualização da informação.
			Foi selecionado os contentores que fizessem parte da tabela Container_CargoManifest e 
			que o id do cargoManifest onde estava colocado esse contentor fosse igual ao ID de 
            Unloading Cargo Manifest associado à tabela das Stops.
			Depois foi feita uma verificação em que o SeqNum da Stop teria que ser igual ao LastSeqNum
			da trip + 1, ou seja, a Trip tem um atributo chamado LastStopSeqNum que indica a ultima paragem
			onde o Ship esteve e que é atualizada sempre que o Ship chega a uma nova paragem. Então,
			LastStopSeqNum + 1 vai indicar qual a proxima paragem dessa viagem.
			Foi feita também uma função em que a diferença é que se manda o id do captain e mostra só dos
			Ships associados a esse capitão
			
			
			
	[US210] As Traffic manager, I need to know which ships will be available on Monday
            next week and their location
			
			
			Para este US foi feito um SELECT do vehicleID e da sua localização. Esta UC foi feita de duas 
			formas. Uma em que se usou Coalesce em que se o ship nunca tivesse feito nenhuma viagem, o
			location.name desse ship iria ser null, então vai-se buscar a localização inicial do ship.
			A outra forma foi usar um CASE em que teria exatamente o mesmo resultado.
			Depois foram feitos full outer joins com as tabelas, trip, stops e location, pois se fizesse
			inner joins, não conseguia ir buscar os ships que nunca fizeram nenhuma viagem. 
			De seguida foram feitas várias verificações, o seqNum das Stops teria que ser o MAXIMO seqNum
			das stops associados a uma viagem para saber se o ship ja acabou a viagem ou se ainda está a meio.
			A data de partida da viagem teria que ser menor que a proxima segunda-feira e a data de chegada também
			ou igual, ou a data de partida começa depois dessa segunda feira, ou entao que nunca tenha feito nenhuma viagem.
			Foi feita também uma função em que se manda como parâmetro a data da proxima segunda feira