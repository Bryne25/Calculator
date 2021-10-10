
package calculator;

import java.util.*; 
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class calc extends javax.swing.JFrame {
    
  
    
    //STRING FORMATTTING
    
    public static String properFormatting(String s, Double a){
    	s = s.replaceAll(" ", "").toLowerCase();
		if (s.endsWith(".")){
			s = s.substring(0, s.length()-1);
		}
		s = s.replaceAll("ans", a.toString());
		s = s.replaceAll("!", "!1");
		List<String> temporaryString = new ArrayList<>();
		Matcher m = Pattern.compile("[^\\d\\w)](?=[+ \\- * \\/ % ^])-\\d+\\.?\\d*").matcher(s);
   	 		while (m.find()) {
   	 			temporaryString.add(m.group());
   	 		}
   	 	if (s.startsWith("-")){
   	 		Matcher match = Pattern.compile("-\\d+\\.?\\d*").matcher(s);
   	 		if (match.find()) {s = s.replaceFirst("-\\d+\\.?\\d*", "(0" + match.group(0) + ")");}
   	 		
   	 	}
   	 	for (int y =0; y < temporaryString.size(); y++){
   	 		String rep1 = temporaryString.get(y).substring(1,temporaryString.get(y).length());
   	 		String rep2 = temporaryString.get(y).substring(0,1);
   	 		s = s.replace(temporaryString.get(y), rep2 + "(" + "0" + rep1+ ")");
   	 	}
   	 	boolean replaced = false;
   	 	while (replaced == false){
			if (s.contains("))")){
				s = s.replaceAll("\\)\\)", "\\)+0\\)");
				continue;
			}
			if (s.contains("((")){
				s = s.replaceAll("\\(\\(", "\\(0+\\(");
				continue;
			}
			replaced = true;
		}
    	return s;
    }
    
    //FIND NUMERS AND OPERANDS
    
    public static List<String> findNumbers(String str){
    	//Find the numbers that you need
    	List<String> withParenthesis = new ArrayList<String>();
    	Matcher m = Pattern.compile("\\(?\\d+\\.?\\d*\\)?").matcher(str);
    	 while (m.find()) {
    		 withParenthesis.add(m.group());
    	 }
    	 return withParenthesis;
    }
    
    public static List<String> findOperands(String str){
    	String[] operands = {"!","-","+","*","/","%","^"};
    	String [] stringArray = str.split("\\(?\\d+\\.?\\d*\\)?");
    	List<String> returned = new ArrayList<String>();
		for (int x = 0; x < stringArray.length; x++){
			for (int y = 0; y < operands.length; y++){
				if (stringArray[x].contains(operands[y])){
					returned.add(operands[y]);
				}
			}
		}
		return returned;
    }
    
    //DEAL WITH PARENTHESIS
    
    public static Double parenthesisFinder(List<String> nums, List<String> opps){
    	int start = -1;
    	int stop = -1;
    	int skips = 0;
    	for (int i = 0; i < nums.size(); i++){
    		if (nums.get(i).contains("(") && start == -1){
    			nums.add(i, nums.get(i).replaceAll("\\(", ""));
    			nums.remove(i+1);
    			start = i;
    		}
    		if (nums.get(i).contains("(") && start != -1){
    			skips++;
    		}
    		if (nums.get(i).contains(")") && skips == 0){
    			stop = i;
    			nums.add(i, nums.get(i).replaceAll("\\)", ""));
        		nums.remove(i+1);
    		} else if (nums.get(i).contains(")") && skips != 0) { skips--; }
    	}
    	if (start == -1 && stop == -1){
    		//no parenthesis were found
    		List<Double> numsAsDubs = new ArrayList<Double>();
    		for (String s : nums) {
    		    numsAsDubs.add(Double.parseDouble(s));
    		}
    		return math(numsAsDubs,opps);
    	} else {
    	
    		try{
    			List<String> tempNumList = new ArrayList<>(nums.subList(start, stop+1));
    			List<String> tempOppList = new ArrayList<>(opps.subList(start, stop));
    		} catch(IllegalArgumentException e){
                        System.out.print("Syntax Error");
    			return 0.0;
    		}finally{}
    		
    		List<String> tempNumList = new ArrayList<>(nums.subList(start, stop+1));
			List<String> tempOppList = new ArrayList<>(opps.subList(start, stop));
			
    		for (int x = start; x < stop; x++){
    			nums.remove(start);
    			opps.remove(start);
    		}
    		nums.remove(start);
    		
    		Double replacement = parenthesisFinder(tempNumList,tempOppList);		
    		nums.add(start,replacement.toString());
    		return parenthesisFinder(nums, opps);
    	}
    }
    
    //DO MATH ON THE INDEXES OF THE LISTS THAT ARE PASSED
    
    public static Double math(List<Double> numbers, List<String> operands){
    	Double temp = 0.0;
    	String[] orderOfOpps = {"!","^","%","*/","+-"};
    	if (numbers.size() <= operands.size()){
                System.out.println("Syntax Error");
    		return 0.0;
    	}
    	int inOrder = 0;
    	int k = 0;
    	while (operands.size() > 0){
    		if (orderOfOpps[inOrder].contains(operands.get(k))){
    			temp = doMath(numbers.get(k),numbers.get(k+1),operands.get(k));
    			numbers.remove(k);
    			numbers.remove(k);
    			numbers.add(k,temp);
    			operands.remove(k);
    		} else{k++;}
    		if (k == operands.size()){
    			k = 0;
    			inOrder++;
    		}
    	} 
    	return numbers.get(0);
    }
    
    public static Double doMath(Double arg1, Double arg2, String compare){
    	Double answer = 0.0;
    	switch (compare){
		case "!": answer = fact(arg1); break;
		case "^": answer = Math.pow(arg1,arg2); break;
		case "%": answer = arg1%arg2; break;
		case "/": answer = arg1/arg2; break;
		case "*": answer = arg1*arg2; break;
		case "+": answer = arg1+arg2; break;
		case "-": answer = arg1-arg2; break;
    	}
    	return answer;
    }
    
    //COMPUTE FACTORIALS
    
    public static double fact(double n){
		if (n <= 1) 
			return 1;
		else 
			return n * fact(n-1);
	}



    public calc() {
        initComponents();
    }
    double first,second,third, result = 0;
   //double resultPermu = 0;
    public String Operation;
    String Answer;
    public double a = 0,b = 0,c = 0,d = 0, ProducNotAns = 1;
    public double answer = 0;
    
    
    
    public static double ceiling(String temp){
       double ceil = Double.parseDouble(temp);
       ceil = Math.ceil(ceil);
       return ceil;
    }
    public static double floor(String temp){
        double floor = Double.parseDouble(temp);
        floor = Math.floor(floor);
        return floor;
    }
    public void add_char(char i){
        String temp = input_field.getText();
        temp += i;
        input_field.setText(temp);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel2 = new javax.swing.JPanel();
        input_field1 = new javax.swing.JTextField();
        num_10 = new javax.swing.JButton();
        num_11 = new javax.swing.JButton();
        num_12 = new javax.swing.JButton();
        num_13 = new javax.swing.JButton();
        num_14 = new javax.swing.JButton();
        num_15 = new javax.swing.JButton();
        num_16 = new javax.swing.JButton();
        num_17 = new javax.swing.JButton();
        num_18 = new javax.swing.JButton();
        num_19 = new javax.swing.JButton();
        dot1 = new javax.swing.JButton();
        delete1 = new javax.swing.JButton();
        clear1 = new javax.swing.JButton();
        divide1 = new javax.swing.JButton();
        multiply1 = new javax.swing.JButton();
        add1 = new javax.swing.JButton();
        sub1 = new javax.swing.JButton();
        equal1 = new javax.swing.JButton();
        cubeRoot1 = new javax.swing.JButton();
        modulo1 = new javax.swing.JButton();
        doubleSum1 = new javax.swing.JButton();
        ceiling1 = new javax.swing.JButton();
        exponent1 = new javax.swing.JButton();
        combination1 = new javax.swing.JButton();
        permutation1 = new javax.swing.JButton();
        DoubleProductNotation1 = new javax.swing.JButton();
        sum1 = new javax.swing.JButton();
        floor1 = new javax.swing.JButton();
        squareRoot1 = new javax.swing.JButton();
        factorial1 = new javax.swing.JButton();
        buttonA1 = new javax.swing.JButton();
        ProductNotation1 = new javax.swing.JButton();
        buttonD1 = new javax.swing.JButton();
        log1 = new javax.swing.JButton();
        buttonC1 = new javax.swing.JButton();
        buttonB1 = new javax.swing.JButton();
        alabel1 = new javax.swing.JLabel();
        blabel1 = new javax.swing.JLabel();
        clabel1 = new javax.swing.JLabel();
        dlabel1 = new javax.swing.JLabel();
        CloseParenthesis1 = new javax.swing.JButton();
        OpenParenthesis1 = new javax.swing.JButton();
        jPanel1 = new javax.swing.JPanel();
        input_field = new javax.swing.JTextField();
        num_1 = new javax.swing.JButton();
        num_4 = new javax.swing.JButton();
        num_7 = new javax.swing.JButton();
        num_2 = new javax.swing.JButton();
        num_5 = new javax.swing.JButton();
        num_8 = new javax.swing.JButton();
        num_6 = new javax.swing.JButton();
        num_9 = new javax.swing.JButton();
        num_3 = new javax.swing.JButton();
        num_0 = new javax.swing.JButton();
        dot = new javax.swing.JButton();
        delete = new javax.swing.JButton();
        clear = new javax.swing.JButton();
        divide = new javax.swing.JButton();
        multiply = new javax.swing.JButton();
        add = new javax.swing.JButton();
        sub = new javax.swing.JButton();
        equal = new javax.swing.JButton();
        cubeRoot = new javax.swing.JButton();
        modulo = new javax.swing.JButton();
        doubleSum = new javax.swing.JButton();
        ceiling = new javax.swing.JButton();
        exponent = new javax.swing.JButton();
        combination = new javax.swing.JButton();
        permutation = new javax.swing.JButton();
        DoubleProductNotation = new javax.swing.JButton();
        sum = new javax.swing.JButton();
        floor = new javax.swing.JButton();
        squareRoot = new javax.swing.JButton();
        factorial = new javax.swing.JButton();
        buttonA = new javax.swing.JButton();
        ProductNotation = new javax.swing.JButton();
        buttonD = new javax.swing.JButton();
        log = new javax.swing.JButton();
        buttonC = new javax.swing.JButton();
        buttonB = new javax.swing.JButton();
        alabel = new javax.swing.JLabel();
        blabel = new javax.swing.JLabel();
        clabel = new javax.swing.JLabel();
        dlabel = new javax.swing.JLabel();
        CloseParenthesis = new javax.swing.JButton();
        OpenParenthesis = new javax.swing.JButton();
        Y = new javax.swing.JButton();
        X = new javax.swing.JButton();
        round = new javax.swing.JButton();

        jPanel2.setBackground(new java.awt.Color(102, 102, 102));
        jPanel2.setDoubleBuffered(false);

        input_field1.setEditable(false);
        input_field1.setFont(new java.awt.Font("Rod", 0, 24)); // NOI18N
        input_field1.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        input_field1.setAutoscrolls(false);
        input_field1.setBorder(null);
        input_field1.setName("input_field"); // NOI18N
        input_field1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                input_field1ActionPerformed(evt);
            }
        });

        num_10.setBackground(new java.awt.Color(51, 51, 51));
        num_10.setFont(new java.awt.Font("Tahoma", 1, 24)); // NOI18N
        num_10.setForeground(new java.awt.Color(255, 255, 255));
        num_10.setText("1");
        num_10.setBorder(null);
        num_10.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                num_10ActionPerformed(evt);
            }
        });

        num_11.setBackground(new java.awt.Color(51, 51, 51));
        num_11.setFont(new java.awt.Font("Tahoma", 1, 24)); // NOI18N
        num_11.setForeground(new java.awt.Color(255, 255, 255));
        num_11.setText("4");
        num_11.setBorder(null);
        num_11.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                num_11ActionPerformed(evt);
            }
        });

        num_12.setBackground(new java.awt.Color(51, 51, 51));
        num_12.setFont(new java.awt.Font("Tahoma", 1, 24)); // NOI18N
        num_12.setForeground(new java.awt.Color(255, 255, 255));
        num_12.setText("7");
        num_12.setBorder(null);
        num_12.setBorderPainted(false);
        num_12.setFocusPainted(false);
        num_12.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                num_12ActionPerformed(evt);
            }
        });

        num_13.setBackground(new java.awt.Color(51, 51, 51));
        num_13.setFont(new java.awt.Font("Tahoma", 1, 24)); // NOI18N
        num_13.setForeground(new java.awt.Color(255, 255, 255));
        num_13.setText("2");
        num_13.setBorder(null);
        num_13.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                num_13ActionPerformed(evt);
            }
        });

        num_14.setBackground(new java.awt.Color(51, 51, 51));
        num_14.setFont(new java.awt.Font("Tahoma", 1, 24)); // NOI18N
        num_14.setForeground(new java.awt.Color(255, 255, 255));
        num_14.setText("5");
        num_14.setBorder(null);
        num_14.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                num_14ActionPerformed(evt);
            }
        });

        num_15.setBackground(new java.awt.Color(51, 51, 51));
        num_15.setFont(new java.awt.Font("Tahoma", 1, 24)); // NOI18N
        num_15.setForeground(new java.awt.Color(255, 255, 255));
        num_15.setText("8");
        num_15.setBorder(null);
        num_15.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                num_15ActionPerformed(evt);
            }
        });

        num_16.setBackground(new java.awt.Color(51, 51, 51));
        num_16.setFont(new java.awt.Font("Tahoma", 1, 24)); // NOI18N
        num_16.setForeground(new java.awt.Color(255, 255, 255));
        num_16.setText("6");
        num_16.setBorder(null);
        num_16.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                num_16ActionPerformed(evt);
            }
        });

        num_17.setBackground(new java.awt.Color(51, 51, 51));
        num_17.setFont(new java.awt.Font("Tahoma", 1, 24)); // NOI18N
        num_17.setForeground(new java.awt.Color(255, 255, 255));
        num_17.setText("9");
        num_17.setBorder(null);
        num_17.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                num_17ActionPerformed(evt);
            }
        });

        num_18.setBackground(new java.awt.Color(51, 51, 51));
        num_18.setFont(new java.awt.Font("Tahoma", 1, 24)); // NOI18N
        num_18.setForeground(new java.awt.Color(255, 255, 255));
        num_18.setText("3");
        num_18.setBorder(null);
        num_18.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                num_18ActionPerformed(evt);
            }
        });

        num_19.setBackground(new java.awt.Color(51, 51, 51));
        num_19.setFont(new java.awt.Font("Tahoma", 1, 24)); // NOI18N
        num_19.setForeground(new java.awt.Color(255, 255, 255));
        num_19.setText("0");
        num_19.setBorder(null);
        num_19.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                num_19ActionPerformed(evt);
            }
        });

        dot1.setBackground(new java.awt.Color(51, 51, 51));
        dot1.setFont(new java.awt.Font("Tahoma", 1, 24)); // NOI18N
        dot1.setForeground(new java.awt.Color(255, 255, 255));
        dot1.setText(".");
        dot1.setBorder(null);
        dot1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                dot1ActionPerformed(evt);
            }
        });

        delete1.setBackground(new java.awt.Color(51, 51, 51));
        delete1.setFont(new java.awt.Font("Tahoma", 1, 24)); // NOI18N
        delete1.setForeground(new java.awt.Color(255, 255, 255));
        delete1.setText("DEL");
        delete1.setBorder(null);
        delete1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                delete1ActionPerformed(evt);
            }
        });

        clear1.setBackground(new java.awt.Color(51, 51, 51));
        clear1.setFont(new java.awt.Font("Tahoma", 1, 24)); // NOI18N
        clear1.setForeground(new java.awt.Color(255, 255, 255));
        clear1.setText("AC");
        clear1.setBorder(null);
        clear1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                clear1ActionPerformed(evt);
            }
        });

        divide1.setBackground(new java.awt.Color(51, 51, 51));
        divide1.setFont(new java.awt.Font("Tahoma", 1, 24)); // NOI18N
        divide1.setForeground(new java.awt.Color(255, 255, 255));
        divide1.setText("÷ ");
        divide1.setBorder(null);
        divide1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                divide1ActionPerformed(evt);
            }
        });

        multiply1.setBackground(new java.awt.Color(51, 51, 51));
        multiply1.setFont(new java.awt.Font("Tahoma", 1, 24)); // NOI18N
        multiply1.setForeground(new java.awt.Color(255, 255, 255));
        multiply1.setText("x");
        multiply1.setBorder(null);
        multiply1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                multiply1ActionPerformed(evt);
            }
        });

        add1.setBackground(new java.awt.Color(51, 51, 51));
        add1.setFont(new java.awt.Font("Tahoma", 1, 24)); // NOI18N
        add1.setForeground(new java.awt.Color(255, 255, 255));
        add1.setText("+");
        add1.setBorder(null);
        add1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                add1ActionPerformed(evt);
            }
        });

        sub1.setBackground(new java.awt.Color(51, 51, 51));
        sub1.setFont(new java.awt.Font("Tahoma", 1, 24)); // NOI18N
        sub1.setForeground(new java.awt.Color(255, 255, 255));
        sub1.setText("-");
        sub1.setBorder(null);
        sub1.setBorderPainted(false);
        sub1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                sub1ActionPerformed(evt);
            }
        });

        equal1.setBackground(new java.awt.Color(51, 51, 51));
        equal1.setFont(new java.awt.Font("Tahoma", 1, 24)); // NOI18N
        equal1.setForeground(new java.awt.Color(255, 255, 255));
        equal1.setText("=");
        equal1.setBorder(null);
        equal1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                equal1ActionPerformed(evt);
            }
        });

        cubeRoot1.setBackground(new java.awt.Color(51, 51, 51));
        cubeRoot1.setFont(new java.awt.Font("Tahoma", 1, 24)); // NOI18N
        cubeRoot1.setForeground(new java.awt.Color(255, 255, 255));
        cubeRoot1.setText("3√");
        cubeRoot1.setBorder(null);
        cubeRoot1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cubeRoot1ActionPerformed(evt);
            }
        });

        modulo1.setBackground(new java.awt.Color(51, 51, 51));
        modulo1.setFont(new java.awt.Font("Tahoma", 1, 24)); // NOI18N
        modulo1.setForeground(new java.awt.Color(255, 255, 255));
        modulo1.setText("%");
        modulo1.setBorder(null);
        modulo1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                modulo1ActionPerformed(evt);
            }
        });

        doubleSum1.setBackground(new java.awt.Color(51, 51, 51));
        doubleSum1.setFont(new java.awt.Font("Tahoma", 1, 24)); // NOI18N
        doubleSum1.setForeground(new java.awt.Color(255, 255, 255));
        doubleSum1.setText("∑∑");
        doubleSum1.setBorder(null);
        doubleSum1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                doubleSum1ActionPerformed(evt);
            }
        });

        ceiling1.setBackground(new java.awt.Color(51, 51, 51));
        ceiling1.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        ceiling1.setForeground(new java.awt.Color(255, 255, 255));
        ceiling1.setText("CEIL");
        ceiling1.setAutoscrolls(true);
        ceiling1.setBorder(null);
        ceiling1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ceiling1ActionPerformed(evt);
            }
        });

        exponent1.setBackground(new java.awt.Color(51, 51, 51));
        exponent1.setFont(new java.awt.Font("Tahoma", 1, 24)); // NOI18N
        exponent1.setForeground(new java.awt.Color(255, 255, 255));
        exponent1.setText("x^y");
        exponent1.setBorder(null);
        exponent1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                exponent1ActionPerformed(evt);
            }
        });

        combination1.setBackground(new java.awt.Color(51, 51, 51));
        combination1.setFont(new java.awt.Font("Tahoma", 1, 24)); // NOI18N
        combination1.setForeground(new java.awt.Color(255, 255, 255));
        combination1.setText("nCr");
        combination1.setBorder(null);
        combination1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                combination1ActionPerformed(evt);
            }
        });

        permutation1.setBackground(new java.awt.Color(51, 51, 51));
        permutation1.setFont(new java.awt.Font("Tahoma", 1, 24)); // NOI18N
        permutation1.setForeground(new java.awt.Color(255, 255, 255));
        permutation1.setText("nPr");
        permutation1.setBorder(null);
        permutation1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                permutation1ActionPerformed(evt);
            }
        });

        DoubleProductNotation1.setBackground(new java.awt.Color(51, 51, 51));
        DoubleProductNotation1.setFont(new java.awt.Font("Tahoma", 1, 24)); // NOI18N
        DoubleProductNotation1.setForeground(new java.awt.Color(255, 255, 255));
        DoubleProductNotation1.setText("ΠΠ");
        DoubleProductNotation1.setBorder(null);
        DoubleProductNotation1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                DoubleProductNotation1ActionPerformed(evt);
            }
        });

        sum1.setBackground(new java.awt.Color(51, 51, 51));
        sum1.setFont(new java.awt.Font("Tahoma", 1, 24)); // NOI18N
        sum1.setForeground(new java.awt.Color(255, 255, 255));
        sum1.setText("∑");
        sum1.setBorder(null);
        sum1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                sum1ActionPerformed(evt);
            }
        });

        floor1.setBackground(new java.awt.Color(51, 51, 51));
        floor1.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        floor1.setForeground(new java.awt.Color(255, 255, 255));
        floor1.setText("FLOOR");
        floor1.setBorder(null);
        floor1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                floor1ActionPerformed(evt);
            }
        });

        squareRoot1.setBackground(new java.awt.Color(51, 51, 51));
        squareRoot1.setFont(new java.awt.Font("Tahoma", 1, 24)); // NOI18N
        squareRoot1.setForeground(new java.awt.Color(255, 255, 255));
        squareRoot1.setText("√");
        squareRoot1.setBorder(null);
        squareRoot1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                squareRoot1ActionPerformed(evt);
            }
        });

        factorial1.setBackground(new java.awt.Color(51, 51, 51));
        factorial1.setFont(new java.awt.Font("Tahoma", 1, 24)); // NOI18N
        factorial1.setForeground(new java.awt.Color(255, 255, 255));
        factorial1.setText("x!");
        factorial1.setBorder(null);
        factorial1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                factorial1ActionPerformed(evt);
            }
        });

        buttonA1.setBackground(new java.awt.Color(51, 51, 51));
        buttonA1.setFont(new java.awt.Font("Tahoma", 1, 24)); // NOI18N
        buttonA1.setForeground(new java.awt.Color(255, 255, 255));
        buttonA1.setText("A");
        buttonA1.setBorder(null);
        buttonA1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonA1ActionPerformed(evt);
            }
        });

        ProductNotation1.setBackground(new java.awt.Color(51, 51, 51));
        ProductNotation1.setFont(new java.awt.Font("Tahoma", 1, 24)); // NOI18N
        ProductNotation1.setForeground(new java.awt.Color(255, 255, 255));
        ProductNotation1.setText("Π");
        ProductNotation1.setBorder(null);
        ProductNotation1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ProductNotation1ActionPerformed(evt);
            }
        });

        buttonD1.setBackground(new java.awt.Color(51, 51, 51));
        buttonD1.setFont(new java.awt.Font("Tahoma", 1, 24)); // NOI18N
        buttonD1.setForeground(new java.awt.Color(255, 255, 255));
        buttonD1.setText("D");
        buttonD1.setBorder(null);
        buttonD1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonD1ActionPerformed(evt);
            }
        });

        log1.setBackground(new java.awt.Color(51, 51, 51));
        log1.setFont(new java.awt.Font("Tahoma", 1, 24)); // NOI18N
        log1.setForeground(new java.awt.Color(255, 255, 255));
        log1.setText("log");
        log1.setBorder(null);
        log1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                log1ActionPerformed(evt);
            }
        });

        buttonC1.setBackground(new java.awt.Color(51, 51, 51));
        buttonC1.setFont(new java.awt.Font("Tahoma", 1, 24)); // NOI18N
        buttonC1.setForeground(new java.awt.Color(255, 255, 255));
        buttonC1.setText("C");
        buttonC1.setBorder(null);
        buttonC1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonC1ActionPerformed(evt);
            }
        });

        buttonB1.setBackground(new java.awt.Color(51, 51, 51));
        buttonB1.setFont(new java.awt.Font("Tahoma", 1, 24)); // NOI18N
        buttonB1.setForeground(new java.awt.Color(255, 255, 255));
        buttonB1.setText("B");
        buttonB1.setBorder(null);
        buttonB1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonB1ActionPerformed(evt);
            }
        });

        alabel1.setText("A =");

        blabel1.setText("B =");

        clabel1.setText("C =");

        dlabel1.setText("D =");

        CloseParenthesis1.setBackground(new java.awt.Color(51, 51, 51));
        CloseParenthesis1.setFont(new java.awt.Font("Tahoma", 1, 24)); // NOI18N
        CloseParenthesis1.setForeground(new java.awt.Color(255, 255, 255));
        CloseParenthesis1.setText(")");
        CloseParenthesis1.setBorder(null);
        CloseParenthesis1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                CloseParenthesis1ActionPerformed(evt);
            }
        });

        OpenParenthesis1.setBackground(new java.awt.Color(51, 51, 51));
        OpenParenthesis1.setFont(new java.awt.Font("Tahoma", 1, 24)); // NOI18N
        OpenParenthesis1.setForeground(new java.awt.Color(255, 255, 255));
        OpenParenthesis1.setText("(");
        OpenParenthesis1.setBorder(null);
        OpenParenthesis1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                OpenParenthesis1ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                        .addGroup(jPanel2Layout.createSequentialGroup()
                            .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(num_10, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 67, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(num_11, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 67, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addGroup(jPanel2Layout.createSequentialGroup()
                                    .addComponent(num_14, javax.swing.GroupLayout.PREFERRED_SIZE, 67, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addComponent(num_16, javax.swing.GroupLayout.PREFERRED_SIZE, 67, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addComponent(multiply1, javax.swing.GroupLayout.PREFERRED_SIZE, 67, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addComponent(divide1, javax.swing.GroupLayout.PREFERRED_SIZE, 67, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addComponent(sum1, javax.swing.GroupLayout.PREFERRED_SIZE, 67, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGroup(jPanel2Layout.createSequentialGroup()
                                    .addComponent(num_13, javax.swing.GroupLayout.PREFERRED_SIZE, 67, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addComponent(num_18, javax.swing.GroupLayout.PREFERRED_SIZE, 67, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addComponent(add1, javax.swing.GroupLayout.PREFERRED_SIZE, 67, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addComponent(sub1, javax.swing.GroupLayout.PREFERRED_SIZE, 67, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addComponent(ProductNotation1, javax.swing.GroupLayout.PREFERRED_SIZE, 67, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                .addComponent(DoubleProductNotation1, javax.swing.GroupLayout.PREFERRED_SIZE, 67, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(doubleSum1, javax.swing.GroupLayout.PREFERRED_SIZE, 67, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGroup(jPanel2Layout.createSequentialGroup()
                            .addComponent(dot1, javax.swing.GroupLayout.PREFERRED_SIZE, 67, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(num_19, javax.swing.GroupLayout.PREFERRED_SIZE, 67, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(equal1, javax.swing.GroupLayout.PREFERRED_SIZE, 67, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(squareRoot1, javax.swing.GroupLayout.PREFERRED_SIZE, 67, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(cubeRoot1, javax.swing.GroupLayout.PREFERRED_SIZE, 67, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(exponent1, javax.swing.GroupLayout.PREFERRED_SIZE, 67, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(permutation1, javax.swing.GroupLayout.PREFERRED_SIZE, 67, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addComponent(num_12, javax.swing.GroupLayout.PREFERRED_SIZE, 67, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(num_15, javax.swing.GroupLayout.PREFERRED_SIZE, 67, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(num_17, javax.swing.GroupLayout.PREFERRED_SIZE, 67, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(clear1, javax.swing.GroupLayout.PREFERRED_SIZE, 67, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(delete1, javax.swing.GroupLayout.PREFERRED_SIZE, 67, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(ceiling1, javax.swing.GroupLayout.PREFERRED_SIZE, 67, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addComponent(alabel1)
                                .addGap(42, 42, 42)
                                .addComponent(blabel1)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(floor1, javax.swing.GroupLayout.PREFERRED_SIZE, 67, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                                .addComponent(clabel1)
                                .addGap(16, 16, 16)))))
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addComponent(modulo1, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(buttonA1, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(log1, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(factorial1, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(combination1, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(buttonC1, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(buttonD1, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(buttonB1, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addComponent(OpenParenthesis1, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(CloseParenthesis1, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE))))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(22, 22, 22)
                        .addComponent(dlabel1)))
                .addContainerGap(34, Short.MAX_VALUE))
            .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel2Layout.createSequentialGroup()
                    .addContainerGap()
                    .addComponent(input_field1, javax.swing.GroupLayout.PREFERRED_SIZE, 614, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap(37, Short.MAX_VALUE)))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(alabel1)
                    .addComponent(blabel1)
                    .addComponent(clabel1)
                    .addComponent(dlabel1))
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(86, 86, 86)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(floor1, javax.swing.GroupLayout.PREFERRED_SIZE, 68, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(CloseParenthesis1, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(OpenParenthesis1, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(ceiling1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 68, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(delete1, javax.swing.GroupLayout.PREFERRED_SIZE, 68, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(clear1, javax.swing.GroupLayout.PREFERRED_SIZE, 68, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(num_17, javax.swing.GroupLayout.PREFERRED_SIZE, 68, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(num_15, javax.swing.GroupLayout.PREFERRED_SIZE, 68, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(num_12, javax.swing.GroupLayout.PREFERRED_SIZE, 68, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                .addGroup(jPanel2Layout.createSequentialGroup()
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(num_11, javax.swing.GroupLayout.PREFERRED_SIZE, 68, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(num_14, javax.swing.GroupLayout.PREFERRED_SIZE, 68, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(num_16, javax.swing.GroupLayout.PREFERRED_SIZE, 68, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel2Layout.createSequentialGroup()
                                    .addGap(5, 5, 5)
                                    .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(divide1, javax.swing.GroupLayout.PREFERRED_SIZE, 68, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(sum1, javax.swing.GroupLayout.PREFERRED_SIZE, 68, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(doubleSum1, javax.swing.GroupLayout.PREFERRED_SIZE, 68, javax.swing.GroupLayout.PREFERRED_SIZE))))
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel2Layout.createSequentialGroup()
                                .addGap(5, 5, 5)
                                .addComponent(multiply1, javax.swing.GroupLayout.PREFERRED_SIZE, 68, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(DoubleProductNotation1, javax.swing.GroupLayout.PREFERRED_SIZE, 68, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(ProductNotation1, javax.swing.GroupLayout.PREFERRED_SIZE, 68, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(sub1, javax.swing.GroupLayout.PREFERRED_SIZE, 68, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(add1, javax.swing.GroupLayout.PREFERRED_SIZE, 68, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(num_18, javax.swing.GroupLayout.PREFERRED_SIZE, 68, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(num_13, javax.swing.GroupLayout.PREFERRED_SIZE, 68, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(num_10, javax.swing.GroupLayout.PREFERRED_SIZE, 68, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(permutation1, javax.swing.GroupLayout.PREFERRED_SIZE, 68, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(exponent1, javax.swing.GroupLayout.PREFERRED_SIZE, 68, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(cubeRoot1, javax.swing.GroupLayout.PREFERRED_SIZE, 68, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(squareRoot1, javax.swing.GroupLayout.PREFERRED_SIZE, 68, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(equal1, javax.swing.GroupLayout.PREFERRED_SIZE, 68, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(num_19, javax.swing.GroupLayout.PREFERRED_SIZE, 68, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(dot1, javax.swing.GroupLayout.PREFERRED_SIZE, 68, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(modulo1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(buttonA1, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(buttonB1, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(log1, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(buttonC1, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(factorial1, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(combination1, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(buttonD1, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addContainerGap())
            .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel2Layout.createSequentialGroup()
                    .addContainerGap()
                    .addComponent(input_field1, javax.swing.GroupLayout.PREFERRED_SIZE, 84, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap(316, Short.MAX_VALUE)))
        );

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Calculator");
        setAlwaysOnTop(true);
        setBackground(new java.awt.Color(0, 0, 0));
        setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        setName("mainFrame"); // NOI18N
        setResizable(false);

        jPanel1.setBackground(new java.awt.Color(102, 102, 102));
        jPanel1.setDoubleBuffered(false);

        input_field.setFont(new java.awt.Font("Rod", 0, 48)); // NOI18N
        input_field.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        input_field.setAutoscrolls(false);
        input_field.setBorder(null);
        input_field.setName("input_field"); // NOI18N
        input_field.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                input_fieldActionPerformed(evt);
            }
        });

        num_1.setBackground(new java.awt.Color(51, 51, 51));
        num_1.setFont(new java.awt.Font("Tahoma", 1, 24)); // NOI18N
        num_1.setForeground(new java.awt.Color(255, 255, 255));
        num_1.setText("1");
        num_1.setBorder(null);
        num_1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                num_1ActionPerformed(evt);
            }
        });

        num_4.setBackground(new java.awt.Color(51, 51, 51));
        num_4.setFont(new java.awt.Font("Tahoma", 1, 24)); // NOI18N
        num_4.setForeground(new java.awt.Color(255, 255, 255));
        num_4.setText("4");
        num_4.setBorder(null);
        num_4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                num_4ActionPerformed(evt);
            }
        });

        num_7.setBackground(new java.awt.Color(51, 51, 51));
        num_7.setFont(new java.awt.Font("Tahoma", 1, 24)); // NOI18N
        num_7.setForeground(new java.awt.Color(255, 255, 255));
        num_7.setText("7");
        num_7.setBorder(null);
        num_7.setBorderPainted(false);
        num_7.setFocusPainted(false);
        num_7.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                num_7ActionPerformed(evt);
            }
        });

        num_2.setBackground(new java.awt.Color(51, 51, 51));
        num_2.setFont(new java.awt.Font("Tahoma", 1, 24)); // NOI18N
        num_2.setForeground(new java.awt.Color(255, 255, 255));
        num_2.setText("2");
        num_2.setBorder(null);
        num_2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                num_2ActionPerformed(evt);
            }
        });

        num_5.setBackground(new java.awt.Color(51, 51, 51));
        num_5.setFont(new java.awt.Font("Tahoma", 1, 24)); // NOI18N
        num_5.setForeground(new java.awt.Color(255, 255, 255));
        num_5.setText("5");
        num_5.setBorder(null);
        num_5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                num_5ActionPerformed(evt);
            }
        });

        num_8.setBackground(new java.awt.Color(51, 51, 51));
        num_8.setFont(new java.awt.Font("Tahoma", 1, 24)); // NOI18N
        num_8.setForeground(new java.awt.Color(255, 255, 255));
        num_8.setText("8");
        num_8.setBorder(null);
        num_8.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                num_8ActionPerformed(evt);
            }
        });

        num_6.setBackground(new java.awt.Color(51, 51, 51));
        num_6.setFont(new java.awt.Font("Tahoma", 1, 24)); // NOI18N
        num_6.setForeground(new java.awt.Color(255, 255, 255));
        num_6.setText("6");
        num_6.setBorder(null);
        num_6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                num_6ActionPerformed(evt);
            }
        });

        num_9.setBackground(new java.awt.Color(51, 51, 51));
        num_9.setFont(new java.awt.Font("Tahoma", 1, 24)); // NOI18N
        num_9.setForeground(new java.awt.Color(255, 255, 255));
        num_9.setText("9");
        num_9.setBorder(null);
        num_9.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                num_9ActionPerformed(evt);
            }
        });

        num_3.setBackground(new java.awt.Color(51, 51, 51));
        num_3.setFont(new java.awt.Font("Tahoma", 1, 24)); // NOI18N
        num_3.setForeground(new java.awt.Color(255, 255, 255));
        num_3.setText("3");
        num_3.setBorder(null);
        num_3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                num_3ActionPerformed(evt);
            }
        });

        num_0.setBackground(new java.awt.Color(51, 51, 51));
        num_0.setFont(new java.awt.Font("Tahoma", 1, 24)); // NOI18N
        num_0.setForeground(new java.awt.Color(255, 255, 255));
        num_0.setText("0");
        num_0.setBorder(null);
        num_0.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                num_0ActionPerformed(evt);
            }
        });

        dot.setBackground(new java.awt.Color(51, 51, 51));
        dot.setFont(new java.awt.Font("Tahoma", 1, 24)); // NOI18N
        dot.setForeground(new java.awt.Color(255, 255, 255));
        dot.setText(".");
        dot.setBorder(null);
        dot.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                dotActionPerformed(evt);
            }
        });

        delete.setBackground(new java.awt.Color(51, 51, 51));
        delete.setFont(new java.awt.Font("Tahoma", 1, 24)); // NOI18N
        delete.setForeground(new java.awt.Color(255, 255, 255));
        delete.setText("DEL");
        delete.setBorder(null);
        delete.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                deleteActionPerformed(evt);
            }
        });

        clear.setBackground(new java.awt.Color(51, 51, 51));
        clear.setFont(new java.awt.Font("Tahoma", 1, 24)); // NOI18N
        clear.setForeground(new java.awt.Color(255, 255, 255));
        clear.setText("AC");
        clear.setBorder(null);
        clear.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                clearActionPerformed(evt);
            }
        });

        divide.setBackground(new java.awt.Color(51, 51, 51));
        divide.setFont(new java.awt.Font("Tahoma", 1, 24)); // NOI18N
        divide.setForeground(new java.awt.Color(255, 255, 255));
        divide.setText("÷ ");
        divide.setBorder(null);
        divide.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                divideActionPerformed(evt);
            }
        });

        multiply.setBackground(new java.awt.Color(51, 51, 51));
        multiply.setFont(new java.awt.Font("Tahoma", 1, 24)); // NOI18N
        multiply.setForeground(new java.awt.Color(255, 255, 255));
        multiply.setText("x");
        multiply.setBorder(null);
        multiply.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                multiplyActionPerformed(evt);
            }
        });

        add.setBackground(new java.awt.Color(51, 51, 51));
        add.setFont(new java.awt.Font("Tahoma", 1, 24)); // NOI18N
        add.setForeground(new java.awt.Color(255, 255, 255));
        add.setText("+");
        add.setBorder(null);
        add.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addActionPerformed(evt);
            }
        });

        sub.setBackground(new java.awt.Color(51, 51, 51));
        sub.setFont(new java.awt.Font("Tahoma", 1, 24)); // NOI18N
        sub.setForeground(new java.awt.Color(255, 255, 255));
        sub.setText("-");
        sub.setBorder(null);
        sub.setBorderPainted(false);
        sub.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                subActionPerformed(evt);
            }
        });

        equal.setBackground(new java.awt.Color(51, 51, 51));
        equal.setFont(new java.awt.Font("Tahoma", 1, 24)); // NOI18N
        equal.setForeground(new java.awt.Color(255, 255, 255));
        equal.setText("=");
        equal.setBorder(null);
        equal.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                equalActionPerformed(evt);
            }
        });

        cubeRoot.setBackground(new java.awt.Color(51, 51, 51));
        cubeRoot.setFont(new java.awt.Font("Tahoma", 1, 24)); // NOI18N
        cubeRoot.setForeground(new java.awt.Color(255, 255, 255));
        cubeRoot.setText("3√");
        cubeRoot.setBorder(null);
        cubeRoot.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cubeRootActionPerformed(evt);
            }
        });

        modulo.setBackground(new java.awt.Color(51, 51, 51));
        modulo.setFont(new java.awt.Font("Tahoma", 1, 24)); // NOI18N
        modulo.setForeground(new java.awt.Color(255, 255, 255));
        modulo.setText("%");
        modulo.setBorder(null);
        modulo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                moduloActionPerformed(evt);
            }
        });

        doubleSum.setBackground(new java.awt.Color(51, 51, 51));
        doubleSum.setFont(new java.awt.Font("Tahoma", 1, 24)); // NOI18N
        doubleSum.setForeground(new java.awt.Color(255, 255, 255));
        doubleSum.setText("∑∑");
        doubleSum.setBorder(null);
        doubleSum.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                doubleSumActionPerformed(evt);
            }
        });

        ceiling.setBackground(new java.awt.Color(51, 51, 51));
        ceiling.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        ceiling.setForeground(new java.awt.Color(255, 255, 255));
        ceiling.setText("CEIL");
        ceiling.setAutoscrolls(true);
        ceiling.setBorder(null);
        ceiling.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ceilingActionPerformed(evt);
            }
        });

        exponent.setBackground(new java.awt.Color(51, 51, 51));
        exponent.setFont(new java.awt.Font("Tahoma", 1, 24)); // NOI18N
        exponent.setForeground(new java.awt.Color(255, 255, 255));
        exponent.setText("x^y");
        exponent.setBorder(null);
        exponent.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                exponentActionPerformed(evt);
            }
        });

        combination.setBackground(new java.awt.Color(51, 51, 51));
        combination.setFont(new java.awt.Font("Tahoma", 1, 24)); // NOI18N
        combination.setForeground(new java.awt.Color(255, 255, 255));
        combination.setText("nCr");
        combination.setBorder(null);
        combination.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                combinationActionPerformed(evt);
            }
        });

        permutation.setBackground(new java.awt.Color(51, 51, 51));
        permutation.setFont(new java.awt.Font("Tahoma", 1, 24)); // NOI18N
        permutation.setForeground(new java.awt.Color(255, 255, 255));
        permutation.setText("nPr");
        permutation.setBorder(null);
        permutation.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                permutationActionPerformed(evt);
            }
        });

        DoubleProductNotation.setBackground(new java.awt.Color(51, 51, 51));
        DoubleProductNotation.setFont(new java.awt.Font("Tahoma", 1, 24)); // NOI18N
        DoubleProductNotation.setForeground(new java.awt.Color(255, 255, 255));
        DoubleProductNotation.setText("ΠΠ");
        DoubleProductNotation.setBorder(null);
        DoubleProductNotation.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                DoubleProductNotationActionPerformed(evt);
            }
        });

        sum.setBackground(new java.awt.Color(51, 51, 51));
        sum.setFont(new java.awt.Font("Tahoma", 1, 24)); // NOI18N
        sum.setForeground(new java.awt.Color(255, 255, 255));
        sum.setText("∑");
        sum.setBorder(null);
        sum.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                sumActionPerformed(evt);
            }
        });

        floor.setBackground(new java.awt.Color(51, 51, 51));
        floor.setFont(new java.awt.Font("Tahoma", 1, 17)); // NOI18N
        floor.setForeground(new java.awt.Color(255, 255, 255));
        floor.setText("FLOOR");
        floor.setBorder(null);
        floor.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                floorActionPerformed(evt);
            }
        });

        squareRoot.setBackground(new java.awt.Color(51, 51, 51));
        squareRoot.setFont(new java.awt.Font("Tahoma", 1, 24)); // NOI18N
        squareRoot.setForeground(new java.awt.Color(255, 255, 255));
        squareRoot.setText("√");
        squareRoot.setBorder(null);
        squareRoot.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                squareRootActionPerformed(evt);
            }
        });

        factorial.setBackground(new java.awt.Color(51, 51, 51));
        factorial.setFont(new java.awt.Font("Tahoma", 1, 24)); // NOI18N
        factorial.setForeground(new java.awt.Color(255, 255, 255));
        factorial.setText("x!");
        factorial.setBorder(null);
        factorial.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                factorialActionPerformed(evt);
            }
        });

        buttonA.setBackground(new java.awt.Color(51, 51, 51));
        buttonA.setFont(new java.awt.Font("Tahoma", 1, 24)); // NOI18N
        buttonA.setForeground(new java.awt.Color(255, 255, 255));
        buttonA.setText("A");
        buttonA.setBorder(null);
        buttonA.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonAActionPerformed(evt);
            }
        });

        ProductNotation.setBackground(new java.awt.Color(51, 51, 51));
        ProductNotation.setFont(new java.awt.Font("Tahoma", 1, 24)); // NOI18N
        ProductNotation.setForeground(new java.awt.Color(255, 255, 255));
        ProductNotation.setText("Π");
        ProductNotation.setBorder(null);
        ProductNotation.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ProductNotationActionPerformed(evt);
            }
        });

        buttonD.setBackground(new java.awt.Color(51, 51, 51));
        buttonD.setFont(new java.awt.Font("Tahoma", 1, 24)); // NOI18N
        buttonD.setForeground(new java.awt.Color(255, 255, 255));
        buttonD.setText("D");
        buttonD.setBorder(null);
        buttonD.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonDActionPerformed(evt);
            }
        });

        log.setBackground(new java.awt.Color(51, 51, 51));
        log.setFont(new java.awt.Font("Tahoma", 1, 24)); // NOI18N
        log.setForeground(new java.awt.Color(255, 255, 255));
        log.setText("log");
        log.setBorder(null);
        log.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                logActionPerformed(evt);
            }
        });

        buttonC.setBackground(new java.awt.Color(51, 51, 51));
        buttonC.setFont(new java.awt.Font("Tahoma", 1, 24)); // NOI18N
        buttonC.setForeground(new java.awt.Color(255, 255, 255));
        buttonC.setText("C");
        buttonC.setBorder(null);
        buttonC.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonCActionPerformed(evt);
            }
        });

        buttonB.setBackground(new java.awt.Color(51, 51, 51));
        buttonB.setFont(new java.awt.Font("Tahoma", 1, 24)); // NOI18N
        buttonB.setForeground(new java.awt.Color(255, 255, 255));
        buttonB.setText("B");
        buttonB.setBorder(null);
        buttonB.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonBActionPerformed(evt);
            }
        });

        alabel.setText("A =");

        blabel.setText("B =");

        clabel.setText("C =");

        dlabel.setText("D =");

        CloseParenthesis.setBackground(new java.awt.Color(51, 51, 51));
        CloseParenthesis.setFont(new java.awt.Font("Tahoma", 1, 24)); // NOI18N
        CloseParenthesis.setForeground(new java.awt.Color(255, 255, 255));
        CloseParenthesis.setText(")");
        CloseParenthesis.setBorder(null);
        CloseParenthesis.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                CloseParenthesisActionPerformed(evt);
            }
        });

        OpenParenthesis.setBackground(new java.awt.Color(51, 51, 51));
        OpenParenthesis.setFont(new java.awt.Font("Tahoma", 1, 24)); // NOI18N
        OpenParenthesis.setForeground(new java.awt.Color(255, 255, 255));
        OpenParenthesis.setText("(");
        OpenParenthesis.setBorder(null);
        OpenParenthesis.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                OpenParenthesisActionPerformed(evt);
            }
        });

        Y.setBackground(new java.awt.Color(51, 51, 51));
        Y.setFont(new java.awt.Font("Tahoma", 1, 24)); // NOI18N
        Y.setForeground(new java.awt.Color(255, 255, 255));
        Y.setText("Y");
        Y.setBorder(null);
        Y.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                YActionPerformed(evt);
            }
        });

        X.setBackground(new java.awt.Color(51, 51, 51));
        X.setFont(new java.awt.Font("Tahoma", 1, 24)); // NOI18N
        X.setForeground(new java.awt.Color(255, 255, 255));
        X.setText("X");
        X.setBorder(null);
        X.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                XActionPerformed(evt);
            }
        });

        round.setBackground(new java.awt.Color(51, 51, 51));
        round.setFont(new java.awt.Font("Tahoma", 1, 24)); // NOI18N
        round.setForeground(new java.awt.Color(255, 255, 255));
        round.setText("INT");
        round.setBorder(null);
        round.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                roundActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                        .addGroup(jPanel1Layout.createSequentialGroup()
                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(num_1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 67, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(num_4, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 67, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addGroup(jPanel1Layout.createSequentialGroup()
                                    .addComponent(num_5, javax.swing.GroupLayout.PREFERRED_SIZE, 67, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addComponent(num_6, javax.swing.GroupLayout.PREFERRED_SIZE, 67, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addComponent(multiply, javax.swing.GroupLayout.PREFERRED_SIZE, 67, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addComponent(divide, javax.swing.GroupLayout.PREFERRED_SIZE, 67, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addComponent(sum, javax.swing.GroupLayout.PREFERRED_SIZE, 67, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGroup(jPanel1Layout.createSequentialGroup()
                                    .addComponent(num_2, javax.swing.GroupLayout.PREFERRED_SIZE, 67, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addComponent(num_3, javax.swing.GroupLayout.PREFERRED_SIZE, 67, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addComponent(add, javax.swing.GroupLayout.PREFERRED_SIZE, 67, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addComponent(sub, javax.swing.GroupLayout.PREFERRED_SIZE, 67, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addComponent(ProductNotation, javax.swing.GroupLayout.PREFERRED_SIZE, 67, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                .addComponent(DoubleProductNotation, javax.swing.GroupLayout.PREFERRED_SIZE, 67, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(doubleSum, javax.swing.GroupLayout.PREFERRED_SIZE, 67, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGroup(jPanel1Layout.createSequentialGroup()
                            .addComponent(dot, javax.swing.GroupLayout.PREFERRED_SIZE, 67, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(num_0, javax.swing.GroupLayout.PREFERRED_SIZE, 67, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(equal, javax.swing.GroupLayout.PREFERRED_SIZE, 67, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(squareRoot, javax.swing.GroupLayout.PREFERRED_SIZE, 67, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(cubeRoot, javax.swing.GroupLayout.PREFERRED_SIZE, 67, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(exponent, javax.swing.GroupLayout.PREFERRED_SIZE, 67, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(permutation, javax.swing.GroupLayout.PREFERRED_SIZE, 67, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(num_7, javax.swing.GroupLayout.PREFERRED_SIZE, 67, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(num_8, javax.swing.GroupLayout.PREFERRED_SIZE, 67, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(num_9, javax.swing.GroupLayout.PREFERRED_SIZE, 67, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(clear, javax.swing.GroupLayout.PREFERRED_SIZE, 67, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(delete, javax.swing.GroupLayout.PREFERRED_SIZE, 67, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(ceiling, javax.swing.GroupLayout.PREFERRED_SIZE, 67, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(alabel)
                                .addGap(42, 42, 42)
                                .addComponent(blabel)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(floor, javax.swing.GroupLayout.PREFERRED_SIZE, 67, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                                .addComponent(clabel)
                                .addGap(16, 16, 16)))))
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(modulo, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(buttonA, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(buttonB, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(factorial, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(combination, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(log, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(round, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addGroup(jPanel1Layout.createSequentialGroup()
                                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addGroup(jPanel1Layout.createSequentialGroup()
                                                .addComponent(buttonC, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(buttonD, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE))
                                            .addGroup(jPanel1Layout.createSequentialGroup()
                                                .addComponent(X, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(Y, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                        .addGap(0, 1, Short.MAX_VALUE))))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(OpenParenthesis, javax.swing.GroupLayout.PREFERRED_SIZE, 76, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(CloseParenthesis, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(22, 22, 22)
                        .addComponent(dlabel)))
                .addGap(0, 10, Short.MAX_VALUE))
            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel1Layout.createSequentialGroup()
                    .addContainerGap()
                    .addComponent(input_field, javax.swing.GroupLayout.PREFERRED_SIZE, 674, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(alabel)
                    .addComponent(blabel)
                    .addComponent(clabel)
                    .addComponent(dlabel))
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(80, 80, 80)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(floor, javax.swing.GroupLayout.PREFERRED_SIZE, 68, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(CloseParenthesis, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(OpenParenthesis, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(ceiling, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 68, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(delete, javax.swing.GroupLayout.PREFERRED_SIZE, 68, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(clear, javax.swing.GroupLayout.PREFERRED_SIZE, 68, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(num_9, javax.swing.GroupLayout.PREFERRED_SIZE, 68, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(num_8, javax.swing.GroupLayout.PREFERRED_SIZE, 68, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(num_7, javax.swing.GroupLayout.PREFERRED_SIZE, 68, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                .addGroup(jPanel1Layout.createSequentialGroup()
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(num_4, javax.swing.GroupLayout.PREFERRED_SIZE, 68, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(num_5, javax.swing.GroupLayout.PREFERRED_SIZE, 68, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(num_6, javax.swing.GroupLayout.PREFERRED_SIZE, 68, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel1Layout.createSequentialGroup()
                                    .addGap(5, 5, 5)
                                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(divide, javax.swing.GroupLayout.PREFERRED_SIZE, 68, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(sum, javax.swing.GroupLayout.PREFERRED_SIZE, 68, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(doubleSum, javax.swing.GroupLayout.PREFERRED_SIZE, 68, javax.swing.GroupLayout.PREFERRED_SIZE))))
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel1Layout.createSequentialGroup()
                                .addGap(5, 5, 5)
                                .addComponent(multiply, javax.swing.GroupLayout.PREFERRED_SIZE, 68, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(DoubleProductNotation, javax.swing.GroupLayout.PREFERRED_SIZE, 68, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(ProductNotation, javax.swing.GroupLayout.PREFERRED_SIZE, 68, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(sub, javax.swing.GroupLayout.PREFERRED_SIZE, 68, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(add, javax.swing.GroupLayout.PREFERRED_SIZE, 68, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(num_3, javax.swing.GroupLayout.PREFERRED_SIZE, 68, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(num_2, javax.swing.GroupLayout.PREFERRED_SIZE, 68, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(num_1, javax.swing.GroupLayout.PREFERRED_SIZE, 68, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(permutation, javax.swing.GroupLayout.PREFERRED_SIZE, 68, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(exponent, javax.swing.GroupLayout.PREFERRED_SIZE, 68, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(cubeRoot, javax.swing.GroupLayout.PREFERRED_SIZE, 68, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(squareRoot, javax.swing.GroupLayout.PREFERRED_SIZE, 68, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(equal, javax.swing.GroupLayout.PREFERRED_SIZE, 68, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(num_0, javax.swing.GroupLayout.PREFERRED_SIZE, 68, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(dot, javax.swing.GroupLayout.PREFERRED_SIZE, 68, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(modulo, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(buttonA, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(buttonB, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(buttonD, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(buttonC, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(log, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(factorial, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(X, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(Y, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(combination, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(round, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addGap(170, 170, 170))
            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel1Layout.createSequentialGroup()
                    .addContainerGap()
                    .addComponent(input_field, javax.swing.GroupLayout.PREFERRED_SIZE, 84, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap(416, Short.MAX_VALUE)))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, 402, javax.swing.GroupLayout.PREFERRED_SIZE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void OpenParenthesisActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_OpenParenthesisActionPerformed
        add_char('(');
    }//GEN-LAST:event_OpenParenthesisActionPerformed

    private void CloseParenthesisActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_CloseParenthesisActionPerformed
        add_char(')');
    }//GEN-LAST:event_CloseParenthesisActionPerformed

    private void buttonBActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonBActionPerformed
        try{
            String temp = input_field.getText();
            b = Double.parseDouble(temp);
            blabel.setText("B = " + String.valueOf(b));
            input_field.setText("");
        }catch(Exception e){
            input_field.setText("Error");
        }
    }//GEN-LAST:event_buttonBActionPerformed

    private void buttonCActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonCActionPerformed
        try{
            String temp = input_field.getText();
            c = Double.parseDouble(temp);
            clabel.setText("C = " + String.valueOf(c));
            input_field.setText("");
        }catch(Exception e){
            input_field.setText("Error");
        }
    }//GEN-LAST:event_buttonCActionPerformed

    private void logActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_logActionPerformed
        try{
            String temp = input_field.getText();
            double t = Double.parseDouble(temp);
            t = Math.log10(t);
            input_field.setText(String.valueOf(t));
        }catch(Exception e){
            input_field.setText("Error");
        }
    }//GEN-LAST:event_logActionPerformed

    private void buttonDActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonDActionPerformed
        try{
            String temp = input_field.getText();
            d = Double.parseDouble(temp);
            dlabel.setText("D = " + String.valueOf(d));
            input_field.setText("");
        }catch(Exception e){
            input_field.setText("Error");
        }
    }//GEN-LAST:event_buttonDActionPerformed

    private void ProductNotationActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ProductNotationActionPerformed
        try {
            double total = 1;
            String temp = input_field.getText();
            for(double i = a; i <=b; i++){
                String fixed = temp.replace("X", String.valueOf(i));
                double ans = 0;
                fixed = properFormatting(fixed, ans);
                ans = parenthesisFinder(findNumbers(fixed), findOperands(fixed));
                total *= ans;
            }
            input_field.setText(String.valueOf(total));
        } catch (Exception e1) {
            input_field.setText(null);
        }    
    }//GEN-LAST:event_ProductNotationActionPerformed

    private void buttonAActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonAActionPerformed

        try{
            String temp = input_field.getText();
            a = Double.parseDouble(temp);
            alabel.setText("A = " + String.valueOf(a));
            input_field.setText("");
        }catch(Exception e){
            input_field.setText("Error");
        }
    }//GEN-LAST:event_buttonAActionPerformed

    private void factorialActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_factorialActionPerformed
        String temp = input_field.getText();
        add_char('!');
        Operation = "!";
    }//GEN-LAST:event_factorialActionPerformed

    private void squareRootActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_squareRootActionPerformed
        String temp = input_field.getText();
        if(!temp.contains("√")){
            add_char('√');
        }
        Operation = "√";
    }//GEN-LAST:event_squareRootActionPerformed

    private void floorActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_floorActionPerformed
        input_field.setText(Double.toString(floor(input_field.getText())));
    }//GEN-LAST:event_floorActionPerformed

    private void sumActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_sumActionPerformed
        try {
            double total = 0;
            String temp = input_field.getText();
            for(double i = a; i <=b; i++){
                String fixed = temp.replace("X", String.valueOf(i));
                double ans = 0;
                fixed = properFormatting(fixed, ans);
                ans = parenthesisFinder(findNumbers(fixed), findOperands(fixed));
                total += ans;
            }
            input_field.setText(String.valueOf(total));
        } catch (Exception e1) {
            input_field.setText(null);
        }        // TODO add your handling code here:
    }//GEN-LAST:event_sumActionPerformed

    private void DoubleProductNotationActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_DoubleProductNotationActionPerformed
        try {
            double total = 1;
            String temp = input_field.getText();
            for(double i = a; i<=b; i++){
                String fixedX = temp.replace("X", String.valueOf(i));
                for(double j = c; j<=d;j++){
                    String fixedY = fixedX.replace("Y", String.valueOf(j));
                    double answer = 0;
                    fixedY = properFormatting(fixedY, answer);
                    answer = parenthesisFinder(findNumbers(fixedY), findOperands(fixedY));
                    total *= answer;
                }
            }
            input_field.setText(String.valueOf(total));
        } catch (Exception e1) {
            input_field.setText("Error");

        }
    }//GEN-LAST:event_DoubleProductNotationActionPerformed

    private void permutationActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_permutationActionPerformed
        String temp = input_field.getText();
        if(!temp.contains("P")){
            add_char('P');
        }
        Operation = "nPr";
    }//GEN-LAST:event_permutationActionPerformed

    private void combinationActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_combinationActionPerformed
        String temp = input_field.getText();
        if(!temp.contains("C")){
            add_char('C');
        }
        Operation = "nCr";
    }//GEN-LAST:event_combinationActionPerformed

    private void exponentActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_exponentActionPerformed
        add_char('^');
    }//GEN-LAST:event_exponentActionPerformed

    private void ceilingActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ceilingActionPerformed
        input_field.setText(Double.toString(ceiling(input_field.getText())));
    }//GEN-LAST:event_ceilingActionPerformed

    private void doubleSumActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_doubleSumActionPerformed
        try {
            double total = 0;
            String temp = input_field.getText();
            for(double i = a; i<=b; i++){
                String fixedX = temp.replace("X", String.valueOf(i));
                for(double j = c; j<=d;j++){
                    String fixedY = fixedX.replace("Y", String.valueOf(j));
                    double answer = 0;
                    fixedY = properFormatting(fixedY, answer);
                    answer = parenthesisFinder(findNumbers(fixedY), findOperands(fixedY));
                    total += answer;
                }
            }
            input_field.setText(String.valueOf(total));
        } catch (Exception e1) {
            input_field.setText("Error");

        }
    }//GEN-LAST:event_doubleSumActionPerformed

    private void moduloActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_moduloActionPerformed
        String temp = input_field.getText();
        if(!temp.contains("%")){
            add_char('%');
        }
        Operation = "%";
    }//GEN-LAST:event_moduloActionPerformed

    private void cubeRootActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cubeRootActionPerformed
        String temp = input_field.getText();
        if(!temp.contains("3√")){
            add_char('3');
            add_char('√');
        }
        Operation = "3√";
    }//GEN-LAST:event_cubeRootActionPerformed

    private void equalActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_equalActionPerformed
        //Double answer = 0.0;
       
        //Operation for Permutation
        if ("nPr".equals(Operation)){
            String str = input_field.getText();
            int strlen = str.length();
            int numstr = str.indexOf('P');

            double n = Double.parseDouble(str.substring(0, numstr));
            double r = Double.parseDouble(str.substring(numstr+1, strlen));
            double numeFactorial = 1,denoFactorial = 1;
            result = 0;
            if(n < r){
                String error = "Math Error";
                input_field.setText(error);
            }else{
                for(int i = 1; i <= n; i++){
                    numeFactorial = numeFactorial * i;
                }
                for(int j = 1; j <= (n-r); j++){
                    denoFactorial*=j;
                }
                result = numeFactorial/denoFactorial;
            }
            input_field.setText(Double.toString(result));
        }

        //Operation for Combination
        if("nCr".equals(Operation)){
            String str = input_field.getText();
            int strlen = str.length();
            int numstr = str.indexOf('C');

            double n = Double.parseDouble(str.substring(0, numstr));
            double r = Double.parseDouble(str.substring(numstr+1, strlen));

            double numeFactorial = 1, denoFactorial = 1, rFactorial = 1;

            if(n < r){
                input_field.setText("Math Error");
            }else{
                for(int i = 1; i <= n; i++){
                    numeFactorial *= i;
                }
                for(int j = 1; j <= (n-r); j++){
                    denoFactorial*=j;
                }
                for(int i = 1; i <= r; i++){
                    rFactorial *= i;
                }
                result = numeFactorial/(rFactorial * denoFactorial);
                input_field.setText(Double.toString(result));
            }
            
        }

        //Operation for square root
        if("√".equals(Operation)){
            String str = input_field.getText();
            int strlen = str.length();
            int numstr = str.indexOf('√');

            double sqrt = Math.sqrt(Double.parseDouble(str.substring(numstr+1, strlen)));

            input_field.setText(Double.toString(sqrt));
        }
        //Operation for cube root
        if("3√".equals(Operation)){
            String str = input_field.getText();
            int strlen = str.length();
            int numstr = str.indexOf('√');

            double cbrt = Math.cbrt(Double.parseDouble(str.substring(numstr+1, strlen)));

            input_field.setText(Double.toString(cbrt));
        }
        
         while(true){
            String string = input_field.getText();
            string = properFormatting(string, answer);
            answer = parenthesisFinder(findNumbers(string), findOperands(string));
            input_field.setText(Double.toString(answer));
            break;
        }
    }//GEN-LAST:event_equalActionPerformed

    private void subActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_subActionPerformed
        add_char('-');
    }//GEN-LAST:event_subActionPerformed

    private void addActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addActionPerformed
        add_char('+');
    }//GEN-LAST:event_addActionPerformed

    private void multiplyActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_multiplyActionPerformed
        add_char('*');
    }//GEN-LAST:event_multiplyActionPerformed

    private void divideActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_divideActionPerformed
        add_char('/');
    }//GEN-LAST:event_divideActionPerformed

    private void clearActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_clearActionPerformed
        // TODO add your handling code here:
        input_field.setText("");
        answer =0; a=0; b=0; c=0; d=0;
    }//GEN-LAST:event_clearActionPerformed

    private void deleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_deleteActionPerformed
        // TODO add your handling code here:
        String temp = input_field.getText();
        if(!temp.isEmpty()){
            temp = temp.substring(0, temp.length()-1);
            input_field.setText(temp);
        }
    }//GEN-LAST:event_deleteActionPerformed

    private void dotActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_dotActionPerformed
        // TODO add your handling code here:
        add_char('.');
    }//GEN-LAST:event_dotActionPerformed

    private void num_0ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_num_0ActionPerformed
        // TODO add your handling code here:
        add_char('0');
    }//GEN-LAST:event_num_0ActionPerformed

    private void num_3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_num_3ActionPerformed
        // TODO add your handling code here:
        add_char('3');
    }//GEN-LAST:event_num_3ActionPerformed

    private void num_9ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_num_9ActionPerformed
        // TODO add your handling code here:
        add_char('9');
    }//GEN-LAST:event_num_9ActionPerformed

    private void num_6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_num_6ActionPerformed
        // TODO add your handling code here:
        add_char('6');
    }//GEN-LAST:event_num_6ActionPerformed

    private void num_8ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_num_8ActionPerformed
        // TODO add your handling code here:
        add_char('8');
    }//GEN-LAST:event_num_8ActionPerformed

    private void num_5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_num_5ActionPerformed
        // TODO add your handling code here:
        add_char('5');
    }//GEN-LAST:event_num_5ActionPerformed

    private void num_2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_num_2ActionPerformed
        // TODO add your handling code here:
        add_char('2');
    }//GEN-LAST:event_num_2ActionPerformed

    private void num_7ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_num_7ActionPerformed
        // TODO add your handling code here:
        add_char('7');
    }//GEN-LAST:event_num_7ActionPerformed

    private void num_4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_num_4ActionPerformed
        // TODO add your handling code here:
        add_char('4');
    }//GEN-LAST:event_num_4ActionPerformed

    private void num_1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_num_1ActionPerformed
        // TODO add your handlg code here:
        add_char('1');
    }//GEN-LAST:event_num_1ActionPerformed

    private void input_fieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_input_fieldActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_input_fieldActionPerformed

    private void input_field1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_input_field1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_input_field1ActionPerformed

    private void num_10ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_num_10ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_num_10ActionPerformed

    private void num_11ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_num_11ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_num_11ActionPerformed

    private void num_12ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_num_12ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_num_12ActionPerformed

    private void num_13ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_num_13ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_num_13ActionPerformed

    private void num_14ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_num_14ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_num_14ActionPerformed

    private void num_15ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_num_15ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_num_15ActionPerformed

    private void num_16ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_num_16ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_num_16ActionPerformed

    private void num_17ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_num_17ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_num_17ActionPerformed

    private void num_18ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_num_18ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_num_18ActionPerformed

    private void num_19ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_num_19ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_num_19ActionPerformed

    private void dot1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_dot1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_dot1ActionPerformed

    private void delete1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_delete1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_delete1ActionPerformed

    private void clear1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_clear1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_clear1ActionPerformed

    private void divide1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_divide1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_divide1ActionPerformed

    private void multiply1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_multiply1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_multiply1ActionPerformed

    private void add1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_add1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_add1ActionPerformed

    private void sub1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_sub1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_sub1ActionPerformed

    private void equal1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_equal1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_equal1ActionPerformed

    private void cubeRoot1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cubeRoot1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cubeRoot1ActionPerformed

    private void modulo1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_modulo1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_modulo1ActionPerformed

    private void doubleSum1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_doubleSum1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_doubleSum1ActionPerformed

    private void ceiling1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ceiling1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_ceiling1ActionPerformed

    private void exponent1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_exponent1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_exponent1ActionPerformed

    private void combination1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_combination1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_combination1ActionPerformed

    private void permutation1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_permutation1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_permutation1ActionPerformed

    private void DoubleProductNotation1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_DoubleProductNotation1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_DoubleProductNotation1ActionPerformed

    private void sum1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_sum1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_sum1ActionPerformed

    private void floor1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_floor1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_floor1ActionPerformed

    private void squareRoot1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_squareRoot1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_squareRoot1ActionPerformed

    private void factorial1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_factorial1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_factorial1ActionPerformed

    private void buttonA1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonA1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_buttonA1ActionPerformed

    private void ProductNotation1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ProductNotation1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_ProductNotation1ActionPerformed

    private void buttonD1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonD1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_buttonD1ActionPerformed

    private void log1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_log1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_log1ActionPerformed

    private void buttonC1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonC1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_buttonC1ActionPerformed

    private void buttonB1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonB1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_buttonB1ActionPerformed

    private void CloseParenthesis1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_CloseParenthesis1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_CloseParenthesis1ActionPerformed

    private void OpenParenthesis1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_OpenParenthesis1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_OpenParenthesis1ActionPerformed

    private void YActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_YActionPerformed
        add_char('Y');
    }//GEN-LAST:event_YActionPerformed

    private void XActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_XActionPerformed
        add_char('X');
    }//GEN-LAST:event_XActionPerformed

    private void roundActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_roundActionPerformed
        try {
            double round=Double.parseDouble(input_field.getText());
            int round1=Integer.parseInt(String.valueOf(Math.round(round)));
            input_field.setText(String.valueOf(round1));
        }
	catch(Exception e1){
            input_field.setText(null);
	}
    }//GEN-LAST:event_roundActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
         try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) { 
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(calc.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(calc.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(calc.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(calc.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } 
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new calc().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton CloseParenthesis;
    private javax.swing.JButton CloseParenthesis1;
    private javax.swing.JButton DoubleProductNotation;
    private javax.swing.JButton DoubleProductNotation1;
    private javax.swing.JButton OpenParenthesis;
    private javax.swing.JButton OpenParenthesis1;
    private javax.swing.JButton ProductNotation;
    private javax.swing.JButton ProductNotation1;
    private javax.swing.JButton X;
    private javax.swing.JButton Y;
    private javax.swing.JButton add;
    private javax.swing.JButton add1;
    private javax.swing.JLabel alabel;
    private javax.swing.JLabel alabel1;
    private javax.swing.JLabel blabel;
    private javax.swing.JLabel blabel1;
    private javax.swing.JButton buttonA;
    private javax.swing.JButton buttonA1;
    private javax.swing.JButton buttonB;
    private javax.swing.JButton buttonB1;
    private javax.swing.JButton buttonC;
    private javax.swing.JButton buttonC1;
    private javax.swing.JButton buttonD;
    private javax.swing.JButton buttonD1;
    private javax.swing.JButton ceiling;
    private javax.swing.JButton ceiling1;
    private javax.swing.JLabel clabel;
    private javax.swing.JLabel clabel1;
    private javax.swing.JButton clear;
    private javax.swing.JButton clear1;
    private javax.swing.JButton combination;
    private javax.swing.JButton combination1;
    private javax.swing.JButton cubeRoot;
    private javax.swing.JButton cubeRoot1;
    private javax.swing.JButton delete;
    private javax.swing.JButton delete1;
    private javax.swing.JButton divide;
    private javax.swing.JButton divide1;
    private javax.swing.JLabel dlabel;
    private javax.swing.JLabel dlabel1;
    private javax.swing.JButton dot;
    private javax.swing.JButton dot1;
    private javax.swing.JButton doubleSum;
    private javax.swing.JButton doubleSum1;
    private javax.swing.JButton equal;
    private javax.swing.JButton equal1;
    private javax.swing.JButton exponent;
    private javax.swing.JButton exponent1;
    private javax.swing.JButton factorial;
    private javax.swing.JButton factorial1;
    private javax.swing.JButton floor;
    private javax.swing.JButton floor1;
    private javax.swing.JTextField input_field;
    private javax.swing.JTextField input_field1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JButton log;
    private javax.swing.JButton log1;
    private javax.swing.JButton modulo;
    private javax.swing.JButton modulo1;
    private javax.swing.JButton multiply;
    private javax.swing.JButton multiply1;
    private javax.swing.JButton num_0;
    private javax.swing.JButton num_1;
    private javax.swing.JButton num_10;
    private javax.swing.JButton num_11;
    private javax.swing.JButton num_12;
    private javax.swing.JButton num_13;
    private javax.swing.JButton num_14;
    private javax.swing.JButton num_15;
    private javax.swing.JButton num_16;
    private javax.swing.JButton num_17;
    private javax.swing.JButton num_18;
    private javax.swing.JButton num_19;
    private javax.swing.JButton num_2;
    private javax.swing.JButton num_3;
    private javax.swing.JButton num_4;
    private javax.swing.JButton num_5;
    private javax.swing.JButton num_6;
    private javax.swing.JButton num_7;
    private javax.swing.JButton num_8;
    private javax.swing.JButton num_9;
    private javax.swing.JButton permutation;
    private javax.swing.JButton permutation1;
    private javax.swing.JButton round;
    private javax.swing.JButton squareRoot;
    private javax.swing.JButton squareRoot1;
    private javax.swing.JButton sub;
    private javax.swing.JButton sub1;
    private javax.swing.JButton sum;
    private javax.swing.JButton sum1;
    // End of variables declaration//GEN-END:variables
}
