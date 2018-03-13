# Ejercicio Bank   [![Build Status](https://travis-ci.org/jmjaime/bank.svg?branch=master)](https://travis-ci.org/jmjaime/bank)

## Consigna
Permitir realizar transacciones entre distintas cuentas, teniendo en cuenta:
- Para transacciones internacionales se cobracomo impuesto, el 5% del valor del monto a transferir a la cuenta origen.
- Para transacciones nacionales, solo se cobra el 1% en caso que las cuentas sean de diferentes bancos.
- Para transacciones nacionales del mismo banco, no se cobra nada.

## Ejecutar el ejercicio
```
mvn spring-boot:run
```

## Datos existentes
- Cuenta default         ID: "12345/67"   Bco: "Santander"  Pais: "Argentina"
- Cuenta Nacional        ID: "16678/67"   Bco: "Santander"  Pais: "Argentina"
- Cuenta Nacional        ID: "56789/32"   Bco: "Citi"       Pais: "Argentina"
- Cuenta Internacional   ID: "54321/81"   Bco: "Itau"       Pais: "Brasil"


## End point
- http://localhost:8080/api/transaction
- http://localhost:8080/api/account
