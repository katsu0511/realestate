package register;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.http.Part;

import bean.CreateIdBean2;
import home.RealestateDAO;

/**
 * Servlet implementation class ImportPaymentServlet
 */
@WebServlet("/import/payment")
@MultipartConfig(
	maxFileSize=10000000,
	maxRequestSize=10000000,
	fileSizeThreshold=10000000
)
public class ImportPaymentServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public ImportPaymentServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		HttpSession session = request.getSession();
		
		if (session.getAttribute("userName") == null || session.getAttribute("password") == null) {
			response.sendRedirect(request.getContextPath() + "/login");
		} else {
			request.getRequestDispatcher("/WEB-INF/app/register/import_payment.jsp").forward(request, response);
		}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		HttpSession session = request.getSession();
		
		if (session.getAttribute("userName") == null || session.getAttribute("password") == null) {
			response.sendRedirect(request.getContextPath() + "/login");
		} else {
			// 文字化け対策
			request.setCharacterEncoding("UTF-8");
			response.setContentType("text/html;charset=UTF-8");
			
			// 接続情報
			RealestateDAO db = new RealestateDAO();
			Connection conn = null;
			
			// SQL
			PreparedStatement pstmt1 = null;
			
			// 送信情報の取得
			Part payment = request.getPart("payment");
			String cmpn_id = (String) session.getAttribute("cmpnid");
			BufferedReader br = null;
			int su1 = 0;

			try {
				// データベース接続情報取得
				conn = db.getConnection();
				
				// csv読み込み
				InputStream is = payment.getInputStream();
				InputStreamReader isr = new InputStreamReader(is);
				br = new BufferedReader(isr);
				String line;
				boolean firstRow = true;
				
				while ((line = br.readLine()) != null) {
					if (firstRow) {
						firstRow = false;
						continue;
					}
					
					String[] data = line.split(",");
					
					// pymtnuを生成
					CreateIdBean2 cib2 = new CreateIdBean2();
					String pymtnu = cib2.getCreatedId("udpy", "pymtnu", cmpn_id);
					
					// SQL実行
					String sql1 = "INSERT INTO udpy(pymtnu,cmpnid,paydat,payenm,rcvamt) VALUES(?,?,?,?,?)";
					pstmt1 = conn.prepareStatement(sql1);
					pstmt1.setString(1, pymtnu);
					pstmt1.setString(2, cmpn_id);
					pstmt1.setString(3, data[1].trim());
					pstmt1.setString(4, data[2].trim().replace("　", " "));
					pstmt1.setString(5, data[3].trim());
					pstmt1.executeUpdate();
					su1++;
				}

			} catch (Exception e) {
				System.out.println(e.getMessage());
			} finally {
				try {
					if (su1 > 0) {
						pstmt1.close();
					}
				} catch (SQLException e) { }
				
				try {
					br.close();
				} catch (Exception e) {
					System.out.println(e.getMessage());
				}
			}
			
			response.sendRedirect(request.getContextPath() + "/index/undecided_payment");
		}
	}

}
