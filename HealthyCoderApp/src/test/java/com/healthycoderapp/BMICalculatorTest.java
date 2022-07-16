package com.healthycoderapp;

import static org.junit.jupiter.api.Assertions.*;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

import org.junit.Assume;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.DisabledOnOs;
import org.junit.jupiter.api.condition.OS;
import org.junit.jupiter.api.function.Executable;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

class BMICalculatorTest {
	
	private String environment="dev";
	
	@BeforeAll
	static void beforeAll()
	{
		System.out.println("Before All unit tests");
	}
	
	@AfterAll
	static void afterAll()
	{
		System.out.println("After All unit tests");
	}

	@Test
	void should_ReturnTrue_When_DietRecommeded() {
//	assertTrue(BMICalculator.isDietRecommended(89.0,1.72));
		
		//given
		double weight=89.0;
		double height=1.72;
		
		//when
		boolean recomended=BMICalculator.isDietRecommended(weight, height);
		
		//then
		assertTrue(recomended);
	}
	
	//-------passing single value--------------
	
	@ParameterizedTest
	@ValueSource(doubles = {89.0,95.0,110.})
	void should_Returntrue_When_SingleValueDietRecommeded(Double coderWeight) {		
		//given
		double weight=coderWeight;
		double height=1.72;
		
		//when
		boolean recomended=BMICalculator.isDietRecommended(weight, height);
		
		//then
		assertTrue(recomended);
	}
	
	//------------passing multiple values-----------
	
	@ParameterizedTest(name="weight={0},height={1}")
	@CsvSource(value = {"89.0,1.72","95.0,1.75","110.0,1.78"})
	void should_Returntrue_When_MultipleValuedietRecommeded(Double coderWeight,Double coderHeight) {		
		//given
		double weight=coderWeight;
		double height=coderHeight;
		
		//when
		boolean recomended=BMICalculator.isDietRecommended(weight, height);
		
		//then
		assertTrue(recomended);
	}
	
	//------------passing multiple values from csv file-----------
	
		@ParameterizedTest(name="weight={0},height={1}")
		@CsvFileSource(resources = "/diet-recommended-input-data.csv",numLinesToSkip = 1)
		void should_Returntrue_When_MultipleValuedFromFileDietRecommeded(Double coderWeight,Double coderHeight) {		
			//given
			double weight=coderWeight;
			double height=coderHeight;
			
			//when
			boolean recomended=BMICalculator.isDietRecommended(weight, height);
			
			//then
			assertTrue(recomended);
		}
	
	@Test
	void should_ReturnFalse_When_DietNotRecommeded() {
		
		//given
		double weight=50.0;
		double height=1.92;
		
		//when
		boolean recomended=BMICalculator.isDietRecommended(weight, height);
		
		//then
		assertFalse(recomended);
	}
	
	@Test
	void should_ThrowArithmeticException_When_HeightZero() {
		
		//given
		double weight=50.0;
		double height=0.0;
		
		//when //import org.junit.jupiter.api.functon
		Executable executable=()->BMICalculator.isDietRecommended(weight, height);
		
		//then
		assertThrows(ArithmeticException.class, executable);
	}
	
	@Test
	void should_ReturnCoderWithWorstBMi_When_CoderListNotEmpty() {
		
		//given
		List<Coder> coders=new ArrayList<>();
		coders.add(new Coder(1.80,60.0));
		coders.add(new Coder(1.82,98.0));
		coders.add(new Coder(1.82,64.7));
		
		//when
		Coder coderWorstBMI=BMICalculator.findCoderWithWorstBMI(coders);
		
		//then
		assertAll(
		()->assertEquals(1.82, coderWorstBMI.getHeight()),
		()->assertEquals(98.0, coderWorstBMI.getWeight())
		);
	}
	
	@Test
	@DisplayName(">>> sample method by DisplayName Annotation")
//	@Disabled  --> it will skip the test case
//	@DisabledOnOs(OS.WINDOWS) --> it will skip the test-case when os is mentioned
	void should_ReturnNullWorstBMICoder_When_CoderListEmpty() {
		
		//given
		List<Coder> coders=new ArrayList<>();
		
		//when
		Coder coderWorstBMI=BMICalculator.findCoderWithWorstBMI(coders);
		
		//then
	
		assertNull(coderWorstBMI);
	
	}
	
	
//------------------new Nested Claass------------------------------------
	
	@Nested
	class NestedClass{
		@Test
		void should_ReturnCorrectBMiScoreArray_When_CoderListNotEmpty() {
			
			//given
			List<Coder> coders=new ArrayList<>();
			coders.add(new Coder(1.80,60.0));
			coders.add(new Coder(1.82,98.0));
			coders.add(new Coder(1.82,64.7));
			double expectedArray[]= {18.52,29.59,19.53};
			
			//when
			double[] bmiScores=BMICalculator.getBMIScores(coders);
			
			//then
			assertArrayEquals(expectedArray, bmiScores);

	}
		
		@Test
		void should_ReturnCoderWithWorstBMIIn1ms_When_CoderListHas10000Elements()
		{
			//given
			//we can skip test-case in different environment
			Assume.assumeTrue(BMICalculatorTest.this.environment.equals("prod")); 
			
			List<Coder> coders=new ArrayList<>();
			for(int i=0;i<10000;i++)
			{
				coders.add(new Coder(1.0+i,10.0+i));
			}
			//when
			Executable executable=() -> BMICalculator.getBMIScores(coders);
			
			//then
			assertTimeout(Duration.ofMillis(4), executable);
			
		}
	}
	
}
