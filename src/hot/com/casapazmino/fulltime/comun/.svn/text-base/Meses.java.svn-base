package com.casapazmino.fulltime.comun;

import java.util.EnumSet;

public enum Meses {
	Enero(0), Febrero(1), Marzo(2), Abril(3), Mayo(4), Junio(5), Julio(6),
	Agosto(7), Septiembre(8), Octubre(9), Noviembre(10), Diciembre(11);

	int mes;

	Meses(int mes) {
		this.mes = mes;
	}

	public int getMes() {
		return mes;
	}

	public static int mesSeleccionado(String mes) {
		for (Meses meses : EnumSet.allOf(Meses.class)) {
			if (meses.toString().equals(mes)) {
				return meses.getMes();
			}
		}
		return 0;
	}

/* Forma Larga
	public static final int mesSeleccionado(String mesSeleccionado) {
		int mes = 0;
		
		switch (Meses.valueOf(mesSeleccionado)) {
		case Enero:
			mes = 0;
			break;
		case Febrero:
			mes = 1;
			break;
		case Marzo:
			mes = 2;
			break;			
		case Abril:
			mes = 3;
			break;
		case Mayo:
			mes = 4;
			break;
		case Junio:
			mes = 5;
			break;
		case Julio:
			mes = 6;
			break;
		case Agosto:
			mes = 7;
			break;
		case Septiembre:
			mes = 8;
			break;
		case Octubre:
			mes = 9;
			break;
		case Noviembre:
			mes = 10;
			break;
		case Diciembre:
			mes = 11;
			break;
		default:
		}
		return mes;
	}
*/
}