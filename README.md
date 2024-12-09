# Accenture
* Autor: Betancur Damián **09-12-2024**
## Proyecto: accenture-challenge-java

<div style="text-align: justify;">
Este proyecto es un servicio de procesamiento de órdenes que permite manejar solicitudes concurrentes utilizando un 
ThreadPoolExecutor configurado para maximizar el rendimiento y la escalabilidad. Está diseñado para manejar cargas 
concurrentes altas y optimizar el uso de recursos del sistema.
</div>

## Caracteristicas
- Procesamiento de órdenes de manera síncrona y asíncrona.
- Configuración flexible de ThreadPoolExecutor 
- Manejo de excepciones en tareas concurrentes.
- Métricas expuestas mediante Spring Actuator y Prometheus.
- Pruebas unitarias, de integración y de rendimiento.

<div>
    <h2>Patrones de Diseño Utilizados</h2>
    <ul>
        <li>
            <strong>Strategy:</strong> Manejo dinámico del estado de órdenes con estrategias en <code>OrderStatusStrategyManager</code>.
        </li>
        <li>
            <strong>Builder:</strong> Construcción de objetos complejos como <code>Order</code> y <code>OrderResponse</code>.
        </li>
        <li>
            <strong>Template Method:</strong> Flujo general de procesamiento de órdenes en <code>processOrderAsync</code>.
        </li>
        <li>
            <strong>Factory:</strong> Creación de instancias como <code>ThreadPoolExecutor</code> en <code>ThreadPoolConfig</code>.
        </li>
        <li>
            <strong>Asynchronous:</strong> Procesamiento concurrente con <code>CompletableFuture</code> y <code>ThreadPoolExecutor</code>.
        </li>
        <li>
            <strong>Proxy:</strong> Simulación de dependencias en pruebas con <code>Mock</code>.
        </li>
        <li>
            <strong>Repository:</strong> Acceso a datos desacoplado mediante <code>OrderRepository</code>.
        </li>
        <li>
            <strong>Singleton:</strong> Gestión de instancias únicas como servicios y repositorios.
        </li>
    </ul>
</div>

## Technologias
- **Java 17**
- **Maven: 3.8**
- **Spring Boot 3.4.0**
- Docker (opcional para herramientas como Prometheus y Grafana).

## Uso

Clonar challenge-orderProcessing del repositorio:

````
git clone https://github.com/damianbetancur/accenture-challenge-java.git
````

Desde el IDE, puede ejecutar 3 diferentes modos modificando `active profile`:

-  **dev**:   puerto 8081.
-  **local**: posee carga de datos por defecto con puerto 8082
-  **prod**: definir una URL de produccion con puerto 8080.


Después de obtener el proyecto localmente, descargue las dependencias de Maven.

## ENDPOINTS
### POST. Credenciales no requeridas
### .../orders/processOrder

<table border="1" style="border-collapse: collapse; width: 100%; text-align: left;">
    <thead>
        <tr style="background-color: #f2f2f2;">
            <th colspan="2" style="text-align: center;">User Story: Ingreso de Orden</th>
        </tr>
    </thead>
    <tbody>
        <tr>
            <td><strong>Como</strong></td>
            <td>Cliente</td>
        </tr>
        <tr>
            <td><strong>Quiero</strong></td>
            <td>Ingresar una orden</td>
        </tr>
        <tr>
            <td><strong>Para</strong></td>
            <td>Que se procese mi pedido</td>
        </tr>
        <tr>
            <td><strong>Detalles de Aceptación</strong></td>
            <td>
                <ul>
                    <li>El cliente debe poder ingresar detalles como:
                        <ul>
                            <li>Identificación de la orden.</li>
                            <li>Detalles del cliente (nombre, ID).</li>
                            <li>Productos solicitados (SKU, cantidad).</li>
                            <li>Monto total de la orden.</li>
                        </ul>
                    </li>
                    <li>El sistema debe validar:
                        <ul>
                            <li>Disponibilidad de productos.</li>
                            <li>Validez de los datos del cliente.</li>
                            <li>Que la orden no haya sido procesada previamente.</li>
                        </ul>
                    </li>
                    <li>Una vez validada la orden:
                        <ul>
                            <li>El sistema cambia el estado a "Procesando".</li>
                            <li>Completa el procesamiento y actualiza el estado a "Completada".</li>
                        </ul>
                    </li>
                    <li>El cliente recibe una confirmación de procesamiento exitoso.</li>
                </ul>
            </td>
        </tr>
        <tr>
            <td><strong>Criterios de Aceptación</strong></td>
            <td>
                <ul>
                    <li><strong>Dado</strong> que el cliente ingresa los detalles correctos de la orden,  
                        <strong>Cuando</strong> el cliente envía la solicitud,  
                        <strong>Entonces</strong> el sistema valida, procesa y devuelve un mensaje de confirmación.</li>
                    <li><strong>Dado</strong> que la orden contiene errores o datos faltantes,  
                        <strong>Cuando</strong> el cliente intenta enviarla,  
                        <strong>Entonces</strong> el sistema notifica los errores y no procesa la orden.</li>
                </ul>
            </td>
        </tr>
        <tr>
            <td><strong>Priorización</strong></td>
            <td>Alta. Este flujo es esencial para el negocio, ya que permite a los clientes realizar pedidos.</td>
        </tr>
    </tbody>
</table>


**Request body**

```yaml
{
  "id_order": "0001",
  "customer_id": "C001",
  "order_amount": 10408.5,
  "order_lines": [
    {
      "sku": "P001",
      "quantity": 2
    },
    {
      "sku": "P002",
      "quantity": 10
    }
  ]

}
```

**Response body**

```yaml
{
  "orderId": "0001",
  "customerId": "C001",
  "totalAmount": 10408.5,
  "status": "Order has been successfully processed"
}
```

## Preguntas
<div style="text-align: justify;">
Si tienes dudas puedes enviar un correo electrónico a betancurdamian@gmail.com
</div>

## Alcance
<div style="text-align: justify;">
Las propiedades de **application.yml** siempre se cargan y al mismo tiempo se complementan con las propiedades de **application-SCOPE.yml**. Si una propiedad está en ambos archivos, la que está configurada en **application-SCOPE.yml** tiene preferencia sobre la propiedad de **application.yml**.
</div>


### Clase Principal

La clase principal de esta aplicación es AccentureChallengeJavaApplication

### Manejo de Errores

También proporcionamos manejo básico para excepciones en la clase GlobalExceptionHandler



