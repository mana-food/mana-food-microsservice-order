# language: pt
Funcionalidade: Gerenciamento de Pedidos
  Como um usuário do sistema
  Eu quero gerenciar pedidos
  Para que eu possa criar, atualizar, consultar e deletar pedidos

  Cenário: Criar um pedido com sucesso usando QR_CODE
    Dado que existem produtos válidos no sistema
    Quando eu criar um pedido com método de pagamento QR_CODE
    Então o pedido deve ser criado com sucesso
    E o pedido deve ter status CREATED
    E o método de pagamento deve ser QR_CODE

  Cenário: Criar um pedido com múltiplos produtos
    Dado que existem produtos válidos no sistema
    Quando eu criar um pedido com 3 produtos diferentes
    Então o pedido deve ser criado com sucesso
    E o pedido deve conter 3 produtos
    E o valor total deve ser calculado corretamente

  Cenário: Atualizar status do pedido
    Dado que existe um pedido no sistema com status CREATED
    Quando eu atualizar o status do pedido para RECEIVED
    Então o pedido deve ter status RECEIVED
    E a data de atualização deve ser registrada

  Cenário: Atualizar status do pedido para READY
    Dado que existe um pedido no sistema com status RECEIVED
    Quando eu atualizar o status do pedido para READY
    Então o pedido deve ter status READY
    E a data de atualização deve ser registrada

  Cenário: Consultar pedido por ID
    Dado que existe um pedido no sistema
    Quando eu consultar o pedido pelo ID
    Então o pedido deve ser retornado com sucesso
    E todos os dados do pedido devem estar corretos

  Cenário: Consultar todos os pedidos com paginação
    Dado que existem 15 pedidos no sistema
    Quando eu consultar todos os pedidos na página 0 com tamanho 10
    Então devem ser retornados 10 pedidos
    E o total de páginas deve ser 2

  Cenário: Consultar pedidos prontos para cozinha
    Dado que existem pedidos com status RECEIVED no sistema
    E que existem pedidos com status CREATED no sistema
    Quando eu consultar pedidos prontos para cozinha
    Então apenas pedidos com status READY devem ser retornados

  Cenário: Confirmar pagamento do pedido
    Dado que existe um pedido no sistema com status CREATED
    Quando eu confirmar o pagamento do pedido
    Então o pedido deve ter status RECEIVED
    E o horário de confirmação deve ser registrado

  Cenário: Deletar um pedido
    Dado que existe um pedido no sistema
    Quando eu deletar o pedido
    Então o pedido deve ser marcado como deletado
    E o pedido não deve aparecer nas consultas

  Cenário: Tentar criar pedido com produto inválido
    Dado que não existem produtos no sistema
    Quando eu tentar criar um pedido com produto inexistente
    Então deve retornar erro de produto não encontrado

  Cenário: Tentar atualizar pedido inexistente
    Quando eu tentar atualizar um pedido que não existe
    Então deve retornar erro de pedido não encontrado

  Cenário: Tentar consultar pedido inexistente
    Quando eu tentar consultar um pedido que não existe
    Então deve retornar pedido não encontrado

