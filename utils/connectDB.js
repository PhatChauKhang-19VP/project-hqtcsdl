const sql = require('mssql/msnodesqlv8');

const connectionString = process.env.ConnectionString;

const conn = async () => {
	try {
		// make sure that any items are correctly URL encoded in the connection string
		await sql.connect(connectionString);
		console.log('connect successfully');
		// const result = await sql.query("select * from Student");
		// console.dir(result);
	} catch (err) {
		console.log('some errors');
	}
};

module.exports = conn;
