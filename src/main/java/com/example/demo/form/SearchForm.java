package com.example.demo.form;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SearchForm {
	
	private LocalDate date;
	private String City;
	
	public void setDate(LocalDate date) {
		
	}

}
