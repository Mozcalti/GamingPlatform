class Participante {
    constructor(idParticipante, nombre, apellidos, correo, academia, ies, carrera, semestre, foto, fechaCreacion, idInstitucion) {
        this.idParticipante = idParticipante;
        this.nombre = nombre;
        this.apellidos = apellidos;
        this.correo = correo;
        this.academia = academia;
        this.ies = ies;
        this.carrera = carrera;
        this.semestre = semestre;
        this.foto = foto;
        this.fechaCreacion = fechaCreacion;
        this.idInstitucion = idInstitucion;
    }
}

export {Participante};

