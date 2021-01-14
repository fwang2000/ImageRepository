import Log from "../../Util";

export default class QueryEvaluator {

    private id: String;

    constructor(id: String) {

        this.id = id;
    }

    public evaluate(fileString: String, query: any): Promise<any[]> {

        return new Promise((resolve, reject) => {

        });
    }
}
