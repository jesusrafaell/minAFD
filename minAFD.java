import java.io.*;

public class HERNANDEZ_JESUS {
static String[] nameStateMinAFD = new String[50];
static String[] zeroMinAFD = new String[50];
static String[] oneMinAFD = new String[50];
static String[] finalsMinAFD = new String[50];
static int nMinAFD;
static int nFinMinAFD;

	public static void main(String[] args) { 
    File archivo_in = null;
		FileReader fr = null;
		BufferedReader br = null;
		try {
			archivo_in = new File ("tarea4.in");   
			fr = new FileReader (archivo_in);
			br = new BufferedReader (fr);
			String linea;
			FileWriter archivo_out = null;
			PrintWriter pw = null;
			try{
				archivo_out = new FileWriter("HERNANDEZ_JESUS.out");
				pw = new PrintWriter(archivo_out);
				linea = br.readLine();
				int casos = Integer.parseInt(linea);
				for(int c  = 0; c  < casos; c++){
					int indice = c + 1;
					pw.println("Caso " + indice + ":");
					//States
					linea = br.readLine();
					String[] nameState = linea.split(" ");
					int n = nameState.length;
					String zero[]= new String[n];
					String one[]= new String[n];
					for(int i = 0; i < nameState.length; i++){
						zero[i] = one[i] = " ";
					}
					//Final State
					linea = br.readLine();
						String[] finals = linea.split(" ");
					//Continue linea
					linea = br.readLine(); 
					boolean first = true;
					while(linea.charAt(0) != 'F'){
						if(first && !changeToRun(linea)){
							int x = 0;
							String[] cadena = linea.split(" ");
							int o = Integer.parseInt(cadena[0]);
							if(cadena[2].charAt(0) == '0'){
								zero[o] = cadena[1];
							}else{
								one[o] = cadena[1];
							}
						}else{						
							if(first){
								//imprimir minimizado
								createMinAFD(nameState, zero, one, finals);
								first = false;
								for(int i = 0; i < nMinAFD; i++){
									pw.print(nameStateMinAFD[i] + " ");
								}
								pw.print('\n');
								//Finales
								for(int i = 0; i < nFinMinAFD; i++){
									pw.print(finalsMinAFD[i] + " ");
								}
								pw.print('\n');
								//Enlaces
								for(int i = 0; i < nMinAFD; i++){
									pw.println(nameStateMinAFD[i] + " " + zeroMinAFD[i] + " 0");
									pw.println(nameStateMinAFD[i] + " " + oneMinAFD[i] + " 1");
								}
							}
							String res = recorrerAFDMin(linea);	
							pw.println(res);
						}
					linea = br.readLine(); 
					}
					pw.println();
				}
			}catch (Exception e) {
				e.printStackTrace();
			} finally{
				try{
					if(null != archivo_out)
						archivo_out.close();
				} catch (Exception e2){
					e2.printStackTrace();
				}
			}
		 } catch(Exception e){
			e.printStackTrace();
		 } finally{
		 	try{
				if(null != fr)
					fr.close();
			}catch (Exception e2){
					e2.printStackTrace();
			}
		 }
   }

	static String recorrerAFDMin(String caso){
		if(caso.equals("NUL")){
			if(isFinals(finalsMinAFD, nameStateMinAFD[0], nFinMinAFD))
				return "Aceptada";
			return "Rechazada";
		}else{
			String actual = nameStateMinAFD[0];
			int iactual = 0;
			for(int i = 0; i < caso.length(); i++){
				if(caso.charAt(i) == '0'){
				 	iactual = ubication(zeroMinAFD[iactual], nameStateMinAFD);
					actual = nameStateMinAFD[iactual];
				}else{
				 	iactual = ubication(oneMinAFD[iactual], nameStateMinAFD);
					actual = nameStateMinAFD[iactual];
				}
			}	
			if(isFinals(finalsMinAFD, actual, nFinMinAFD)){
				return "Aceptada";
			}else{
				return "Rechazada";
			}
		}
	}

	static int  ubication(String actual, String[] block){
		for(int i = 0; i < nMinAFD; i++){
			if(actual.equals(block[i])){
				return i;
			}
		}
		return 0;
	}

	static void createMinAFD(String[] nameState, String[] zero, String[] one, String[] finals){
		int n = nameState.length;
		int nF = finals.length;
		//Create Semi-Matriz
		char[][] semiMatriz = new char[n][n];
		//Inicializar SemiMtriz
		for(int i = 0; i < n; i++){
			for(int j = 0; j < n; j++){
				semiMatriz[i][j] = 'B';
			}	
		}
		//Sin finals o no finales
		int	nAux = 0;
		for(int i = 0; i < n-1; i++){
			int j;
			for(j = n-1; j > nAux; j--){
			if(isFinals(finals,nameState[i],nF) != isFinals(finals,nameState[j],nF)){	
					semiMatriz[i][j]='X';
				}else{
					semiMatriz[i][j]=' ';
				}
			}	
			nAux++;
		}

		//Minimizacion de la tabla
		nAux = 0;
		int jaux, iaux;
		int result;
		String[][] posibles = new String[n][n];
		int count = 0;
		for(int i = 0; i < n-1; i++){
			int j;
			for(j = n-1; j > nAux; j--){
				if(semiMatriz[i][j] != 'X' && semiMatriz[i][j] != 'B'){
					result = distinguibles(semiMatriz, nameState, one, zero, i, j);
					if(result == 0){
						if(semiMatriz[i][j] == '*'){	
							String linea = posibles[i][j];
							String[] auxOP = linea.split("-");
							int paux = Integer.parseInt(auxOP[0]);
							int saux = Integer.parseInt(auxOP[1]);
							semiMatriz[paux][saux] = 'X';
						}
						semiMatriz[i][j] = 'X';
					}else if(result == 1){
						//con zero
						iaux = Integer.parseInt(zero[i]);
						jaux = Integer.parseInt(zero[j]);
					//	System.out.println(iaux + " " + jaux + " Posible--> " + nameState[i] + "-" + nameState[j]);
						semiMatriz[iaux][jaux] = '*';
						posibles[iaux][jaux] = nameState[i] + "-" + nameState[j];
						//con one
						iaux = Integer.parseInt(one[i]);
						jaux = Integer.parseInt(one[j]);	
			//			System.out.println(iaux + " " + jaux + " Posible--> " + nameState[i] + "-" + nameState[j]);
						semiMatriz[iaux][jaux] = '*';
						posibles[iaux][jaux] = nameState[i] + "-" + nameState[j];
						//con one
					}else if(result == 2){
				//			System.out.println("Iguales en 1: " + nameState[i] + " " + nameState[j]);
							//Iguales en 1 solo insert en 0	
							iaux = Integer.parseInt(zero[i]);
							jaux = Integer.parseInt(zero[j]);
							semiMatriz[iaux][jaux] = '*';
							posibles[iaux][jaux] = nameState[i] + "-" + nameState[j];
					}else if(result == 3){
			//				System.out.println("Iguales en 0: " + nameState[i] + " " + nameState[j]);
							//Iguales en 0 solo intert en 1
							iaux = Integer.parseInt(one[i]);
							jaux = Integer.parseInt(one[j]);
							semiMatriz[iaux][jaux] = '*';
							posibles[iaux][jaux] = nameState[i] + "-" + nameState[j];
					}else{
			//			System.out.println("Iguales en ambos: " + nameState[i] + " " + nameState[j]);
						//iguales en ambos
						//Nada
					}
				}	
			}
		}

		compileAFD(nameState, zero, one, finals, semiMatriz);
//*++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
	
	}


	static void compileAFD(String[] nameState, String[] zero, String[] one, String[] finals, char[][] semiMatriz){
		int	nAux = 0;
		int n = nameState.length;
		
/*		
		//ImprimIR semiMabla
		for(int i = 0; i < n-1; i++){
			int j;
				System.out.print(i + ")");
			for(j = n-1; j > nAux; j--){
				if(semiMatriz[i][j] != 'X' && semiMatriz[i][j] != 'B'){	
					System.out.print(nameState[i] + "-" + nameState[j] + "|");
				}else{
					System.out.print(semiMatriz[i][j] + "|");
				}
			}	
			System.out.print(semiMatriz[i][j]);
			System.out.print('\n');
			nAux++;
		} 
*/
//***********+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++i

		String[] states = new String[n];
		int count = 0;
		nAux = 0;
		for(int i = 0; i < n-1; i++){
			int j;
			for(j = n-1; j > nAux; j--){
				if(semiMatriz[i][j] != 'X' && semiMatriz[i][j] != 'B'){
					//Create new State
					//Si ninguno ha sido agregado agrego creo un nuevo estado
					if(validarCreate(i, j, nameState, states, count) || count == 0){
						states[count] = nameState[i] + "-" + nameState[j];
						count++;
		//				System.out.println("Create -> " + states[count-1]);
					}else{
							//Si i ha sido agregado, agrego j donde esta i
						if(validarOne(nameState[i], states , count)){
							int position = getPosition(nameState[i], states, count);
							states[position] = createState(states[position], nameState[j]);
		//					System.out.println("Position :" + position + " agrega-> " + nameState[j]);
						}else{
							//si i no ha sido agregado, agrego i donde esta j	
						}
					}
				}
			}	
			nAux++;
		}
		//Agregar todos los estados que no se hayan agregado 
		//Estados que son unicos
		for(int i = 0; i < n; i++){
			if(!validarOne(nameState[i], states, count)){
				states[count] = nameState[i];
				count++;
			}
		}

		//Ordenar States 
		for(int i = 0; i < count; i++){
			String[] aux = states[i].split("-");
			String k = aux[0];
			String actualS = aux[0];
			int posi = i;
			int actual = Integer.parseInt(k);
			for(int j = i+1; j < count; j++){
				String[] aux2 = states[j].split("-");
				int iterator = Integer.parseInt(aux2[0]);
				if(actual > iterator){
					actual = iterator;
					posi = j;
					actualS = states[j];
				}
			}
			if(!k.equals(actualS)){
				states[posi] = states[i];
				states[i] = actualS;
			}
		}

		//Colocar a los arreglos zero y one los nuevos estados
		for(int i = 0; i < n; i++){
				//Arr zero
				int aux = getPosition(zero[i], states, count);
				zero[i] = states[aux];
				//Arr One
				aux = getPosition(one[i], states, count);
				one[i] = states[aux];
		}	

		//Agregar estados el AFDmin
		nMinAFD = count;
		for(int i = 0; i < count; i ++){
			nameStateMinAFD[i] = states[i];
			String[] aux = states[i].split("-");
			int estado = Integer.parseInt(aux[0]);
			//transicion con Zero
			oneMinAFD[i] = one[estado];
			//transicion con One
			zeroMinAFD[i] = zero[estado];
		}

		//Crear arreglo de finales
		int conta = 0;
		for(int i = 0; i < finals.length; i++){
			int aux = getPositionFinal(finals[i], states, count);
			if(aux != -1){
				if(states[aux] != "F"){
					finalsMinAFD[conta] = states[aux];
					conta++;
					states[aux] = "F";
				}
			}
		}
		nFinMinAFD = conta;
		//Imprimir estados
/*		
		for(int i = 0; i < nMinAFD; i++){
			System.out.print(nameStateMinAFD[i] + " " +  zeroMinAFD[i] + " " + oneMinAFD[i]);
			if(isFinals(finalsMinAFD, nameStateMinAFD[i], nFinMinAFD)){
				System.out.println(" es final");
			}else{
				System.out.println();
			}
		}
*/
	}

	
	static int getPositionFinal(String nameState, String[] states ,int nS){
		for(int i = 0; i < nS; i++){
				String[] aux = states[i].split("-");
				for(int j = 0; j < aux.length; j++){
						if(nameState.equals(aux[j])){
							return i;
						}
				}
		}
		return -1;
	}

	static int getPosition(String nameState, String[] states ,int nS){
		for(int i = 0; i < nS; i++){
				String[] aux = states[i].split("-");
				for(int j = 0; j < aux.length; j++){
						if(nameState.equals(aux[j])){
							return i;
						}
				}
		}
		return 0;
	}
	
	static boolean validarOne(String nameState, String[] states ,int nS){
		for(int i = 0; i < nS; i++){
				String[] aux = states[i].split("-");
				for(int j = 0; j < aux.length; j++){
						if(nameState.equals(aux[j])){
//							System.out.println(nameState + " ya esta agreado");
							return true;
						}
				}
		}
		return false;
	
	}

	static boolean validarCreate(int x, int y, String[] nameStates, String[] states, int nS){
		
		for(int i = 0; i < nS; i++){
				String[] aux = states[i].split("-");
				for(int j = 0; j < aux.length; j++){
						if(nameStates[x].equals(aux[j]) || nameStates[y].equals(aux[j])){
		//					System.out.println(nameStates[x] + " " + nameStates[y] + " uno de los dos ya esta agreado");
							return false;
						}
				}
		}
		return true;
	}

	static String createState(String states, String nameState){
//		System.out.println("Entry " + nameState + " -> " + states);
		int aux2 = Integer.parseInt(nameState);
		String[] auxStates = states.split("-");
		String[] auxStates2 = new String[auxStates.length + 1];
		int count = 0;
		boolean entry = false;
		for(int a = 0; a < auxStates.length; a++){
			int aux1 = Integer.parseInt(auxStates[a]);
			if(nameState.equals(auxStates[a])){
//					System.out.println(nameState);
				return states;
			}
			if(aux1 > aux2 && !entry){
				auxStates2[count] = nameState;
				//	system.out.println(auxstates2[count]);
				count++;
				auxStates2[count] = auxStates[a];
		//			system.out.println(auxstates2[count]);
				count++;
				entry = true;
			}else{
				auxStates2[count] = auxStates[a];
	//			System.out.println(auxStates[count]);
				count++;
			}
		}
		if(!entry){
			auxStates2[count] = nameState;
			count++;
		}
		//states = compile -> auxStates2
		states = auxStates2[0];
		for(int i = 1; i < count; i++){
			states = states + "-" + auxStates2[i];
		}	
		return states;	
	}


	static int distinguibles(char[][] semiMatriz, String[] nameState, String[] one, String[] zero, int i, int j){
			//Con ZERO
			int iaux = Integer.parseInt(zero[i]);
			int jaux = Integer.parseInt(zero[j]);
			if (iaux > jaux){
				int aux = iaux;
				iaux = jaux;
				jaux = aux;
			}
			boolean iguales = false;
			if(iaux == jaux){
				iguales = true;
			}
			if((semiMatriz[iaux][jaux] != 'X' && semiMatriz[iaux][jaux] != 'B') || iguales){
				//System.out.print(nameState[i] + " " + nameState[j]);
				//System.out.println(" Con 0: " + iaux + " " + jaux + " ->" + semiMatriz[i][j]);
				//Con ONE
				iaux = Integer.parseInt(one[i]);
				jaux = Integer.parseInt(one[j]);
				if (iaux > jaux){
					int aux = iaux;
					iaux = jaux;
					jaux = aux;
				}
				if(!iguales && iaux == jaux){ //Iguales en 1
					return 2;
				}else if(iguales && !(iaux == jaux)){ //Iguales en 0
					if(semiMatriz[iaux][jaux] == 'X' || semiMatriz[iaux][jaux] == 'B')
						return 0;
					else
						return 3;
				}else if(iguales && iaux == jaux){ //Iguales en ambos
					return 4;
				}
				//Cuando sus estados ady no son iguales
				if(semiMatriz[iaux][jaux] != 'X' && semiMatriz[iaux][jaux] != 'B'){
					return 1;
				}else{
					return 0;
				}
			}else{	
				return 0;
			}
			//Con UNO
	}

	static int getFinal(String[] finals, String value, int n){
		for(int i = 0; i < n; i++){
			if(value.equals(finals[i]))
				return i;
		}
		return 0;
	}

	static boolean isFinals(String[] finals, String value, int n){
		for(int i = 0; i < n; i++){
			if(value.equals(finals[i]))
				return true;
		}
		return false;
	}

	static boolean changeToRun(String linea){
		for(int i = 0; i < linea.length(); i++){
			if(linea.charAt(i) == ' '){
				return false;
			}
		}
		return true;
	}

}
