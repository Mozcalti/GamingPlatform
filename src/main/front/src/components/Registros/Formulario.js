import React, {useState} from 'react'
import { Formik } from "formik";

const Formulario = () => {
	const [formularioEnviado, cambiarFormularioEnviado] = useState(false)
  return (
	<>
	<Formik
	initialValues={{
		nombre: '',
		apellido: '',
		correo: ''
	}}
	validate={(valores) => {
		let errores = {}

		//Validacion nombre
		if(!valores.nombre) {
			errores.nombre = 'Por favor ingresa un nombre'
		} else if(!/^[a-zA-ZÀ-ÿ\s]{1,40}$/.test(valores.nombre)) {
			errores.nombre = 'El nombre solo puede contener letras y espacios'
		}

		//Validacion de apellido
		if(!valores.apellido) {
			errores.apellido = 'Por favor ingresa un apellido'
		} else if(!/^[a-zA-ZÀ-ÿ\s]{1,40}$/.test(valores.nombre)) {
			errores.nombre = 'El Apellido solo puede contener letras y espacios'
		}

		//Validacion correo
		if(!valores.correo) {
			errores.correo = 'Por favor ingresa un correo electronico'
		} else if(!/^[a-zA-Z0-9_.+-]+@[a-zA-Z0-9-]+\.[a-zA-Z0-9-.]+$/.test(valores.correo)) {
			errores.correo = 'El correo solo puede contener letras, numeros, putos y guion bajos'
		}

		//Validacion de academia
		if(!valores.academia) {
			errores.academia = 'Por favor ingresa una academia'
		} 

		//Validacion de IES
		if(!valores.IES) {
			errores.IES = 'Por favor ingresa un IES'
		}

		return errores
	}}
	onSubmit={(valores, {resetForm}) => {
		resetForm()
		//Aqui puedo hacer la llamada al api me conecto y envio los valores
		console.log("formulario enviado")
		cambiarFormularioEnviado(true)
		setTimeout(() => cambiarFormularioEnviado(false), 3000)
	}}
	>
		{( {values, errors, touched, handleSubmit, handleChange, handleBlur} ) => (
			<form className='formulario' onSubmit={handleSubmit}>
			<div>
				<label htmlFor='nombre'>Nombre</label>
				<input
				type="text" 
				id ="nombre" 
				placeholder="Nombre" 
				value={values.nombre} onChange={handleChange} onBlur={handleBlur} />
				{touched.nombre && errors.nombre && <div className="error">{errors.nombre}</div>}
			</div>

			<div>
					<label htmlFor="apellido">Apellido</label>
					<input
						type="text"
						name="apellido"
						placeholder="Apellido"
						id="nombre"
						value={values.apellido} onChange={handleChange} onBlur={handleBlur} />
						{touched.apellido && errors.apellido && <div className="error">{errors.apellido}</div>}
			</div>

			<div>
				<label htmlFor='correo'>Correo</label>
				<input 
				type="email" 
				id ="correo" 
				placeholder="Correo" 
				value={values.correo} onChange={handleChange} onBlur={handleBlur}/>
				{touched.correo && errors.correo && <div className="error">{errors.correo}</div>}
			</div>

			<div>
					<label htmlFor="academia">Academia</label>
					<input
						type="text"
						name="academia"
						placeholder="Academia"
						id="correo"
						value={values.academia} onChange={handleChange} onBlur={handleBlur} />
						{touched.academia && errors.academia && <div className="error">{errors.academia}</div>}
			</div>

			<div>
					<label htmlFor="IES">IES</label>
					<input
						type="text"
						name="IES"
						placeholder="IES"
						id="nombre"
						value={values.IES} onChange={handleChange} onBlur={handleBlur} />
						{touched.IES && errors.IES && <div className="error">{errors.IES}</div>}
			</div>
			<button type="submit">Enviar</button>	
			{formularioEnviado && <p className='exito'>Formulario enviado con exito</p>}
		</form>
		)}
		
	</Formik>
		
	</>
  )
}

export default Formulario