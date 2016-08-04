function partida(campo,campo2)
{
	var texto="";
	var tamano;
	var a="";
	var b="";

	alert("texto 1: "+campo);
	alert("texto 2: "+campo2);
	alert("valor texto 1: "+document.getElementById(campo).value);
	
	texto=document.getElementById(campo).value;
	tamano=document.getElementById(campo).value.length;

	for(i=(tamano-1);i>=0;i--)
	{
		if(texto[i]!='-')
		{
			a=a+''+texto[i];
		}
		else
		{
			break;
		}
	}
	tamano=a.length;
	alert("Tamano tomado desde a: "+tamano);
	for(i=(tamano-1);i>=0;i--)
	{
		alert("Valor de i desde for 2: "+i);
		b=b+''+a[i];
	}
	document.getElementById(campo2).value=b;
}