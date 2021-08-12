package com.terium.siccam.exception;

	public class CustomExcepcion extends Exception{

		
		private static final long serialVersionUID = 1L;
		
		
		public CustomExcepcion(){
			super();
		}
		
		public CustomExcepcion(String mensaje, Throwable causa){
			super(mensaje, causa);
		};		
		
		public CustomExcepcion(String mensaje){
			super(mensaje);
		};
		
		
}
