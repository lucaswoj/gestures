report.pdf: report.tex
	tex2pdf report.tex

bib: report.bib
	biber report
	pdflatex report.tex
	biber report
	pdflatex report.tex
