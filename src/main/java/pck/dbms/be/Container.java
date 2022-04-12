package pck.dbms.be;

import pck.dbms.be.partner.Partner;
import pck.dbms.be.partner.PartnerBranch;
import pck.dbms.be.product.Product;

import java.util.ArrayList;
import java.util.HashMap;

public class Container {
    public static HashMap<String, Partner> partners = new HashMap<>();
    public static HashMap<String, PartnerBranch> partnerBranches = new HashMap<>();
    public static HashMap<String, Product> products = new HashMap<>();
}
