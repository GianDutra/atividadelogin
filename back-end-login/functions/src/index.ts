import * as functions from "firebase-functions";
import * as admin from "firebase-admin";

const app = admin.initializeApp();
const db = app.firestore();
const colUsuario = db.collection("usuarios");

interface CallableResponse{
    status: string,
    message: string,
    payload: JSON
  }

interface usuarios{
  nome: string,
  email: string,
}


/**
 * Função que analisa se um usuário é válido para ser gravado no banco.
 * Exemplo de validação na entrada. Complemente com as regras que achar
 * importante.
 * @param {usuarios} p - Objeto produto a ser validado.
 * @return {number} - Retorna 0 se válido ou o código de erro.
 **/
function analyzeUser(u: usuarios) : number {
    if (!u.nome){
        return 1; 
    }
    if (!u.email){
        return 2;
    }
    return 0;
}

/**
 * Função que dado o código de erro obtido na analyzeProduct,
 * devolve uma mensagem
 * @param {number} code - Código do erro
 * @return {string} - String com a mensagem de erro.
 */
function getErrorMessage(code: number) : string {
    let message = "";
    switch (code) {
        case 1: {
            message = "Preencha o campo: nome.";
            break;
        }
        case 2: {
            message = "Preencha o campo: e-mail."
        }
    }
    return message;
}
//Função que cadastra um Usuário
export const addUsuario = functions
    .region("southamerica-east1")
    .https.onCall(async(data, context) => {
        let result: CallableResponse;

        // com o uso do logger, podemos monitorar os erros e o que há.
        functions.logger.info("addUsuario - Iniciada.")
        // criando o objeto que representa o produto (baseado nos parametros)
        const u = {
            nome: data.nome,
            email: data.email
        }
        // inclua aqui a validacao.
        const errorCode = analyzeUser(u);
        const errorMessage = getErrorMessage(errorCode);
        if (errorCode > 0) {
            // gravar o erro no log e preparar o retorno.
            functions.logger.error("addUsuario" + " - Erro ao cadastrar novo usuário." + errorCode.toString()),

            result = {
                status: "ERROR",
                message: errorMessage,
                payload: JSON.parse(JSON.stringify({docId: null})),   
            };
            console.log(result);
        } else {
            // cadastrar o usuário pois está ok.
            const docRef = await colUsuario.add(u);
            result = {
                status: "SUCCESS",
                message: "Usuário cadastrado com sucesso.",
                payload: JSON.parse(JSON.stringify({docId: docRef.id.toString()})),
            };
            functions.logger.error("addUsuarios - Novo usuário cadastrado com sucesso.")
        }

        // Retornando o objeto result.
        return result;
    
    });
