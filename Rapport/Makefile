all: clean
	pdflatex main.tex
	bibtex references
	pdflatex main.tex
	pdflatex main.tex

clean:
	rm -f *.aux *.out *.synctex.gz *.toc
