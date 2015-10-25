package passagem_aerea.android.dominando.passagem_aerea.Main;

import java.io.Serializable;

/**
 * Created by Maria on 25/10/2015.
 */

    public class ModelPassagem implements Comparable <ModelPassagem>, Serializable {
        private String nome;
        private String origem;
        private String destino;

        public static final String NAO_ENCONTRADA = "Não encontrada.";

        public ModelPassagem (String nome,String origem, String destino) {

            this.origem = origem;
            this.destino = destino;
            this.nome = nome;
        }

        public String getNome() {
            return nome;
        }

        public String getOrigem() {
            return origem;
        }

        public String getDestino() {
            return destino;
        }

        @Override
        public String toString() {
            return "{" +
                    ", nome='" + nome + '\'' +
                    ", cor='" + origem + '\'' +
                    ", pais='" + destino + '\'' +
                    '}';
        }

        @Override
        public int compareTo(ModelPassagem modelPassagem) {
            if (nome.equals(modelPassagem.getNome())
                    && origem.equals(modelPassagem.getOrigem())
                    && destino.equals(modelPassagem.getDestino())) {
                return 0;
            }
            return this.getNome().compareTo(modelPassagem.getNome());
        }
    }


