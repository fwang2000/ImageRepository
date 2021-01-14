import Log from "../../Util";
import QueryParser from "./QueryParser";
const fs = require("fs");

export default class QueryEvaluator {

    private id: String;

    constructor(id: String) {

        this.id = id;
    }

    public evaluate(fileString: String, query: any): Promise<any[]> {

        return new Promise((resolve, reject) => {

            fs.readFile(fileString, (err: Error, data: any) => {

                if (err) {

                    return reject(err);
                }

                let queryResult = this.process(data, query);

                return resolve(queryResult);
            });
        });
    }

    private process(data: any, query: any): any[] {

        const datasetToQuery = JSON.parse(data);
        return this.getResult(datasetToQuery, query);
    }

    private getResult(dataset: any[], query: any): any[] {

        const parser: QueryParser = new QueryParser();
        const where: any = query["WHERE"];
        const options: any = query["OPTIONS"];

        let filterResult: any;

        if (Object.keys.length == 0) {

            return this.emptyWhere(dataset);

        }

        return parser.filter(dataset, where);
    }

    private emptyWhere(dataset: any[]): any[] {

        let result: String[] = [];

        for (let image of dataset) {

            result.push(image["base64"]);
        }
        return result;
    }
}
