package com.politrons.application

object JsonUtils {

  val addPaymentCommand =
    """
      |{"amount":"amount","currency":"currency","paymentId":"paymentId","debtorParty":{"accountName":"accountName","accountNumber":"accountNumber","accountType":0.0,"address":"address","bankId":"bankId","name":"name"},"sponsorParty":{"accountNumber":"accountName","bankId":"bankId","bankIdCode":"bankCode"},"beneficiaryParty":{"accountName":"accountName","accountNumber":"accountNumber","accountType":0.0,"address":"address","bankId":"bankId","name":"name"},"paymentPurpose":"paymentPurpose","paymentType":"paymentType","processingDate":"processingDate","reference":"reference","schemePaymentSubType":"schemePaymentSubType","schemePaymentType":"schemePaymentType"}
    """.stripMargin
val hello =
  """
    |{"message":"hello"}
  """.stripMargin

}
