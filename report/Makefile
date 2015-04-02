report.pdf: report.md
	./tex2pdf report.tex

bib: report.bib
	biber report
	pdflatex report.tex
	biber report
	pdflatex report.tex
