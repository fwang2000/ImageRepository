import Log from "../Util";
import QueryEvaluator from "./helpers/QueryEvaluator";

const fs = require("fs");

export default class QuerySystem {

    public performQuery(id: string, query: Object): Promise<any[]> {

        let pathString: String = "../../data/" + id + "-dataset.txt";
        let queryEvaluator: QueryEvaluator = new QueryEvaluator(id);

        return new Promise((fulfill, reject) => {

            if (!fs.existsSync(pathString)) {

                return reject();
            }

            try {

                queryEvaluator
                    .evaluate(pathString, query)
                    .then((result: any) => {

                        return fulfill(result);
                    })
                    .catch((err: any) => {

                        Log.trace(err);
                    });

            } catch (err) {

                return reject(err);
            }
        });
    }
}
