<#assign PurchaseOrder = doc.PurchaseOrder>
{
  "PurchaseOrder": [
    {
      "CustomerName": "${PurchaseOrder.CustomerName}",
      "CustomerAddress": "${PurchaseOrder.CustomerAddress}",
      "DeliveryNotes": "${PurchaseOrder.DeliveryNotes}",
      "_PurchaseOrderNumber": "${PurchaseOrder.@PurchaseOrderNumber}"
    }
  ]
}