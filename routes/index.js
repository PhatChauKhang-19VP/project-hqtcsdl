var express = require('express');
var router = express.Router();

const sql = require('mssql/msnodesqlv8');

/* GET home page. */
router.get('/', function (req, res, next) {
	res.render('index', { title: 'Express' });
});

router.post('/query', async (req, res, next) => {
	const query = req.body.query;
	console.log(req.body);
	try {
		const result = await sql.query(query);
		res.json(result);
	} catch (err) {
		res.json({ error: err.message });
	}
});
module.exports = router;
