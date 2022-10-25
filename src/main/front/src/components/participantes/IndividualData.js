import React from 'react'

export const IndividualData = ({individualExcelData}) => {
    return (
        <>
            <th>{individualExcelData.Id}</th>
            <th>{individualExcelData.Nombre}</th>
            <th>{individualExcelData.Apellido}</th>
            <th>{individualExcelData.Correo}</th>
            <th>{individualExcelData.Academia}</th>
            <th>{individualExcelData.IES}</th>
        </>
    )
}
