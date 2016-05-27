How to use this program:
1. You can use this program in terminal mode with no argument
   1.1 Example: java -jar AddressNormalizer.jar
2. You can use this program in GUI mode with one argument "G"
   2.1 Example: java -jar AddressBirnakuzer.jar G

How to use the terminal:
	If you selected single entry mode:
		1. Enter the street address
		2. Enter the city 
		3. Enter the state
		4. Enter the zip5
		5. Enter the zip4 (optional)

	If you selected the batch file mode:
		1. Enter the input file location
		   1.1 For testing purpose, enter "dataset/dataset_2016_04_14.txt" without quotes
		2. Enter the output file location for valid addresses
		   2.1 For testing purpose, enter "outputs/parsedAddresses.txt" without quotes
		3. Enter the output file location for invalid addresses
		   3.1 For testing purpose, enter "outputs/unparsedAddresses.txt" without quotes
    
	If you selected quit:
		1. The program closes itself