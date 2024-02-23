package me.astero.companions.util;

public class FormatNumbers {
	
	private String formatedNumber;

	
	public String format(long number)
	{
		String numberDecimal;
		int thousand = 1000, million = 1000000, billion = 1000000000;
		

		if(String.valueOf(number).length() >= 4 && String.valueOf(number).length() < 7)
		{
			if(number % thousand == 0)
			{
				numberDecimal = "";
			}
			else
			{
				try
				{
					numberDecimal = "." + String.valueOf(number % thousand).substring(0, 3);
				}
				catch(StringIndexOutOfBoundsException tooShort)
				{
					numberDecimal = "";
				}
			}
			
			formatedNumber = String.valueOf(number / thousand) + numberDecimal + "K";
		}
		else if(String.valueOf(number).length() >= 7 && String.valueOf(number).length() < 10)
		{
			if(number % million == 0)
			{
				numberDecimal = "";
			}
			else
			{
				try
				{
					numberDecimal = "." + String.valueOf(number % thousand).substring(0, 3);
				}
				catch(StringIndexOutOfBoundsException tooShort)
				{
					numberDecimal = "";
				}
			}
			
			formatedNumber = String.valueOf(number / million) + numberDecimal + "M";
		}
		else if(String.valueOf(number).length() >= 10)
		{
			if(number % billion == 0)
			{
				numberDecimal = "";
			}
			else
			{
				try
				{
					numberDecimal = "." + String.valueOf(number % thousand).substring(0, 3);
				}
				catch(StringIndexOutOfBoundsException tooShort)
				{
					numberDecimal = "";
				}
			}
			
			formatedNumber = String.valueOf(number / billion) + numberDecimal + "B";
		}
		else
		{
			formatedNumber = String.valueOf(number);
		}
		

		
		return formatedNumber;
	}

}
